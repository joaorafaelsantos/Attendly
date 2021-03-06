package pt.attendly.attendly.firebase;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import pt.attendly.attendly.LoginActivity;
import pt.attendly.attendly.MainActivity;

/**
 * Created by Daniel on 05/04/2018.
 */

public class uploadFiles {

    private static StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public static void upload(final Context con, Uri filePath){
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(con);
            progressDialog.setTitle("Carregar");
            progressDialog.show();

            StorageReference riversRef = storageReference.child(LoginActivity.loggedUser.getId());
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            manageData.updateUserImage(downloadUrl);

                            //and displaying a success toast
                            Toast.makeText(con.getApplicationContext(), "Imagem alterada com sucesso!", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(con.getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("A carregar " + ((int) progress) + "%...");
                        }
                    });
        }


}
