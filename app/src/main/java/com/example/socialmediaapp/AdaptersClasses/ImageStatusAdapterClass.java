package com.example.socialmediaapp.AdaptersClasses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialmediaapp.Activities.ImageCommentPage;
import com.example.socialmediaapp.Activities.TextCommentPage;
import com.example.socialmediaapp.ModelClasses.Model_ImageStatus;
import com.example.socialmediaapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ImageStatusAdapterClass extends FirestoreRecyclerAdapter<Model_ImageStatus, ImageStatusAdapterClass.ImageStatusViewHolderClass> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ImageStatusAdapterClass(@NonNull FirestoreRecyclerOptions<Model_ImageStatus> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ImageStatusViewHolderClass imageStatusViewHolderClass, int i, @NonNull Model_ImageStatus model_imageStatus) {
        imageStatusViewHolderClass.userEmailTV.setText(model_imageStatus.getUseremail());
        imageStatusViewHolderClass.statusDateTV.setText(model_imageStatus.getCurrentdatetime());
        imageStatusViewHolderClass.imagesStatusDescriptionTV.setText(model_imageStatus.getStatus());
        imageStatusViewHolderClass.heartCountTV.setText(Integer.toString(model_imageStatus.getNooflove()));
        imageStatusViewHolderClass.hahaCountTV.setText(Integer.toString(model_imageStatus.getNoofhaha()));
        imageStatusViewHolderClass.sadCountTV.setText(Integer.toString(model_imageStatus.getNoofsad()));
        imageStatusViewHolderClass.noOfCommentsTV.setText(Integer.toString(model_imageStatus.getNoofcomments()));
        String linkOfProfileImage = model_imageStatus.getProfileurl();
        String linkOFImageStatus = model_imageStatus.getStatusimgurl();

        Glide.with(imageStatusViewHolderClass.userProfileIV.getContext()).load(linkOfProfileImage)
                .into(imageStatusViewHolderClass.userProfileIV);
        Glide.with(imageStatusViewHolderClass.imageStatusIV.getContext()).load(linkOFImageStatus)
                .into(imageStatusViewHolderClass.imageStatusIV);

        imageStatusViewHolderClass.heartIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth objFirebaseAuth = FirebaseAuth.getInstance();

                if (objFirebaseAuth != null) {
                    String userEmail = objFirebaseAuth.getCurrentUser().getEmail();
                    String documentID = getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).getId();

                    FirebaseFirestore objFirebaseFirestore = FirebaseFirestore.getInstance();
                    DocumentReference objDocumentReference = objFirebaseFirestore.collection("ImageStatus")
                            .document(documentID).collection("Emotions")
                            .document(userEmail);
                    objDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult().exists()) {
                                String currentFlag = task.getResult().getString("currentflag");

                                if (currentFlag.equals("love")) objDocumentReference.update("currentflag", "love");
                                else if (currentFlag.equals("haha")) {
                                    Long totalHearts = (Long) getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).get("nooflove");
                                    totalHearts++;

                                    getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition())
                                            .getReference().update("nooflove", totalHearts);

                                    objDocumentReference.update("currentflag", "love");
                                    Long totalHaha = (Long) getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).get("noofhaha");
                                    totalHaha--;
                                    getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition())
                                            .getReference().update("noofhaha", totalHaha);
                                } else if (currentFlag.equals("sad")) {
                                    Long totalHearts = (Long) getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).get("nooflove");
                                    totalHearts++;

                                    getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition())
                                            .getReference().update("nooflove", totalHearts);

                                    objDocumentReference.update("currentflag", "love");
                                    Long totalSad = (Long) getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).get("noofsad");
                                    totalSad--;
                                    getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).getReference()
                                            .update("noofsad", totalSad);
                                }
                            } else {
                                Map<String, Object> objectMap = new HashMap<>();
                                objectMap.put("currentflag", "love");

                                objFirebaseFirestore.collection("ImageStatus")
                                        .document(documentID).collection("Emotions")
                                        .document(userEmail)
                                        .set(objectMap);

                                Long totalHearts = (Long) getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).get("nooflove");
                                totalHearts++;

                                getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition())
                                        .getReference().update("nooflove", totalHearts);

                                objDocumentReference.update("currentflag", "love");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(imageStatusViewHolderClass.heartIV.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else Toast.makeText(imageStatusViewHolderClass.heartIV.getContext(), "Giriş yapmış bir kullanıcı yok", Toast.LENGTH_SHORT).show();
            }
        });

        imageStatusViewHolderClass.hahaIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth objFirebaseAuth = FirebaseAuth.getInstance();

                if (objFirebaseAuth != null) {
                    String userEmail = objFirebaseAuth.getCurrentUser().getEmail();
                    String documentID = getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).getId();

                    FirebaseFirestore objFirebaseFirestore = FirebaseFirestore.getInstance();
                    DocumentReference objDocumentReference = objFirebaseFirestore.collection("ImageStatus")
                            .document(documentID).collection("Emotions")
                            .document(userEmail);
                    objDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult().exists()) {
                                String currentFlag = task.getResult().getString("currentflag");

                                if (currentFlag.equals("haha")) objDocumentReference.update("currentflag", "haha");
                                else if (currentFlag.equals("love")) {
                                    Long totalHaha = (Long) getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).get("noofhaha");
                                    totalHaha++;

                                    getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition())
                                            .getReference().update("noofhaha", totalHaha);

                                    objDocumentReference.update("currentflag", "haha");
                                    Long totalLove = (Long) getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).get("nooflove");
                                    totalLove--;
                                    getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition())
                                            .getReference().update("nooflove", totalLove);
                                } else if (currentFlag.equals("sad")) {
                                    Long totalHaha = (Long) getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).get("noofhaha");
                                    totalHaha++;

                                    getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition())
                                            .getReference().update("noofhaha", totalHaha);

                                    objDocumentReference.update("currentflag", "haha");
                                    Long totalSad = (Long) getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).get("noofsad");
                                    totalSad--;
                                    getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).getReference()
                                            .update("noofsad", totalSad);
                                }
                            } else {
                                Map<String, Object> objectMap = new HashMap<>();
                                objectMap.put("currentflag", "haha");

                                objFirebaseFirestore.collection("ImageStatus")
                                        .document(documentID).collection("Emotions")
                                        .document(userEmail)
                                        .set(objectMap);

                                Long totalHaha = (Long) getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).get("noofhaha");
                                totalHaha++;

                                getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition())
                                        .getReference().update("noofhaha", totalHaha);

                                objDocumentReference.update("currentflag", "haha");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(imageStatusViewHolderClass.hahaIV.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else Toast.makeText(imageStatusViewHolderClass.hahaIV.getContext(), "Giriş yapmış bir kullanıcı yok", Toast.LENGTH_SHORT).show();
            }
        });

        imageStatusViewHolderClass.sadIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth objFirebaseAuth = FirebaseAuth.getInstance();

                if (objFirebaseAuth != null) {
                    String userEmail = objFirebaseAuth.getCurrentUser().getEmail();
                    String documentID = getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).getId();

                    FirebaseFirestore objFirebaseFirestore = FirebaseFirestore.getInstance();
                    DocumentReference objDocumentReference = objFirebaseFirestore.collection("ImageStatus")
                            .document(documentID).collection("Emotions")
                            .document(userEmail);
                    objDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult().exists()) {
                                String currentFlag = task.getResult().getString("currentflag");

                                if (currentFlag.equals("sad")) objDocumentReference.update("currentflag", "sad");
                                else if (currentFlag.equals("love")) {
                                    Long totalSad = (Long) getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).get("noofsad");
                                    totalSad++;

                                    getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition())
                                            .getReference().update("noofsad", totalSad);

                                    objDocumentReference.update("currentflag", "sad");
                                    Long totalLove = (Long) getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).get("nooflove");
                                    totalLove--;
                                    getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition())
                                            .getReference().update("nooflove", totalLove);
                                } else if (currentFlag.equals("haha")) {
                                    Long totalSad = (Long) getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).get("noofsad");
                                    totalSad++;

                                    getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition())
                                            .getReference().update("noofsad", totalSad);

                                    objDocumentReference.update("currentflag", "sad");
                                    Long totalHaha = (Long) getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).get("noofhaha");
                                    totalHaha--;
                                    getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).getReference()
                                            .update("noofhaha", totalHaha);
                                }
                            } else {
                                Map<String, Object> objectMap = new HashMap<>();
                                objectMap.put("currentflag", "sad");

                                objFirebaseFirestore.collection("ImageStatus")
                                        .document(documentID).collection("Emotions")
                                        .document(userEmail)
                                        .set(objectMap);

                                Long totalHaha = (Long) getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).get("noofsad");
                                totalHaha++;

                                getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition())
                                        .getReference().update("noofsad", totalHaha);

                                objDocumentReference.update("currentflag", "sad");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(imageStatusViewHolderClass.sadIV.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else Toast.makeText(imageStatusViewHolderClass.sadIV.getContext(), "Giriş yapmış bir kullanıcı yok", Toast.LENGTH_SHORT).show();
            }
        });

        imageStatusViewHolderClass.commentIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String documentID = getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).getId();
                Context objectContext = imageStatusViewHolderClass.commentIV.getContext();
                Intent objectIntent = new Intent(objectContext, ImageCommentPage.class);
                objectIntent.putExtra("documentId", documentID);
                objectContext.startActivity(objectIntent);
            }
        });

        imageStatusViewHolderClass.deleteImageStatusIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth objectFirebaseAuth = FirebaseAuth.getInstance();
                if (objectFirebaseAuth.getCurrentUser().getEmail().equals(model_imageStatus.getUseremail())) {
                    FirebaseFirestore objectFirebaseFirestore = FirebaseFirestore.getInstance();
                    objectFirebaseFirestore.collection("ImageStatus")
                            .document(getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).getId())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(imageStatusViewHolderClass.deleteImageStatusIV.getContext(), "Statü silindi", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(imageStatusViewHolderClass.deleteImageStatusIV.getContext(), "Statü silinemedi", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else Toast.makeText(imageStatusViewHolderClass.deleteImageStatusIV.getContext(), "Bu statüyü silme yetkiniz yok", Toast.LENGTH_SHORT).show();
            }
        });

        imageStatusViewHolderClass.favoriteImageStatusIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String documentId = getSnapshots().getSnapshot(imageStatusViewHolderClass.getAdapterPosition()).getId();
                FirebaseFirestore objectFirebaseFirestore = FirebaseFirestore.getInstance();

                DocumentReference objectDocumentReference = objectFirebaseFirestore.collection("ImageStatus")
                        .document(documentId);
                objectDocumentReference.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String emailOfUser = documentSnapshot.getString("useremail");
                                String statusImageURL = documentSnapshot.getString("statusimageurl");
                                String status = documentSnapshot.getString("status");
                                String profileURLOfUser = documentSnapshot.getString("profileurl");
                                String dateOfStatus = documentSnapshot.getString("currentdatetime");
                                FirebaseAuth objectFirebaseAuth = FirebaseAuth.getInstance();

                                Map<String, Object> objectMap = new HashMap<>();
                                objectMap.put("useremail", emailOfUser);
                                objectMap.put("statusimageurl", statusImageURL);
                                objectMap.put("status", status);
                                objectMap.put("profileurl", profileURLOfUser);
                                objectMap.put("currentdatetime", dateOfStatus);

                                if (objectFirebaseAuth != null) {
                                    String currentUserEmail = objectFirebaseAuth.getCurrentUser().getEmail();
                                    objectFirebaseFirestore.collection("UserFavorite")
                                            .document(currentUserEmail).collection("FavoriteImageStatus")
                                            .document(documentId).set(objectMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(imageStatusViewHolderClass.favoriteImageStatusIV.getContext(), "Favoriye eklendi", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(imageStatusViewHolderClass.favoriteImageStatusIV.getContext(), "Favoriye eklenemedi", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else Toast.makeText(imageStatusViewHolderClass.favoriteImageStatusIV.getContext(), "Giriş yapmış bir kullanıcı yok", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
    }

    @NonNull
    @Override
    public ImageStatusViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageStatusViewHolderClass(LayoutInflater.from(parent.getContext()).inflate(R.layout.model_image_status, parent, false));
    }

    public class ImageStatusViewHolderClass extends RecyclerView.ViewHolder {
        ImageView imageStatusIV, favoriteImageStatusIV, deleteImageStatusIV, userProfileIV, heartIV, hahaIV, sadIV, commentIV;
        TextView userEmailTV, statusDateTV, imagesStatusDescriptionTV, heartCountTV, sadCountTV, hahaCountTV, noOfCommentsTV;

        public ImageStatusViewHolderClass(@NonNull View itemView) {
            super(itemView);

            imageStatusIV = itemView.findViewById(R.id.model_image_status_imageStatusIV);
            userEmailTV = itemView.findViewById(R.id.model_image_status_userEmailTV);
            statusDateTV = itemView.findViewById(R.id.model_image_status_dateOfStatusTV);
            imagesStatusDescriptionTV = itemView.findViewById(R.id.model_image_status_imageStatusDesTV);
            heartCountTV = itemView.findViewById(R.id.model_image_status_heartCountTV);
            sadCountTV = itemView.findViewById(R.id.model_image_status_sadCountTV);
            hahaCountTV = itemView.findViewById(R.id.model_image_status_hahaCountTV);
            noOfCommentsTV = itemView.findViewById(R.id.model_image_status_commentCountTV);
            favoriteImageStatusIV = itemView.findViewById(R.id.model_image_status_favoriteImageStatusIV);
            deleteImageStatusIV = itemView.findViewById(R.id.model_image_status_deleteIV);
            userProfileIV = itemView.findViewById(R.id.model_image_status_profilePicIV);
            hahaIV = itemView.findViewById(R.id.model_image_status_hahaIV);
            heartIV = itemView.findViewById(R.id.model_image_status_heartIV);
            sadIV = itemView.findViewById(R.id.model_image_status_sadIV);
            commentIV = itemView.findViewById(R.id.model_image_status_commentIV);
        }
    }
}
