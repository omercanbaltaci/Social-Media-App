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
import com.example.socialmediaapp.Activities.TextCommentPage;
import com.example.socialmediaapp.AppClasses.AddNotifications;
import com.example.socialmediaapp.ModelClasses.Model_TextStatus;
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

public class TextStatusAdapterClass extends FirestoreRecyclerAdapter<Model_TextStatus, TextStatusAdapterClass.TextStatusViewHolder> {
    public TextStatusAdapterClass(@NonNull FirestoreRecyclerOptions<Model_TextStatus> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TextStatusViewHolder textStatusViewHolder, int i, @NonNull Model_TextStatus model_textStatus) {
        textStatusViewHolder.userStatusTV.setText(model_textStatus.getUseremail());
        textStatusViewHolder.dateTimeTV.setText(model_textStatus.getCurrentdatetime());
        textStatusViewHolder.userStatusTV.setText(model_textStatus.getStatus());
        textStatusViewHolder.heartCountTV.setText(Integer.toString(model_textStatus.getNooflove()));
        textStatusViewHolder.hahaCountTV.setText(Integer.toString(model_textStatus.getNoofhaha()));
        textStatusViewHolder.sadCountTV.setText(Integer.toString(model_textStatus.getNoofsad()));
        textStatusViewHolder.commentCountTV.setText(Integer.toString(model_textStatus.getNoofcomments()));
        String linkOfProfileImage = model_textStatus.getProfileurl();

        Glide.with(textStatusViewHolder.profileIV.getContext())
                .load(linkOfProfileImage).into(textStatusViewHolder.profileIV);

        AddNotifications objectAddNotifications = new AddNotifications();

        textStatusViewHolder.heartIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth objFirebaseAuth = FirebaseAuth.getInstance();

                if (objFirebaseAuth != null) {
                    String userEmail = objFirebaseAuth.getCurrentUser().getEmail();
                    String documentID = getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).getId();

                    FirebaseFirestore objFirebaseFirestore = FirebaseFirestore.getInstance();
                    DocumentReference objDocumentReference = objFirebaseFirestore.collection("TextStatus")
                            .document(documentID).collection("Emotions")
                            .document(userEmail);
                    objDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult().exists()) {
                                String currentFlag = task.getResult().getString("currentflag");
                                objectAddNotifications.generateNotification(userEmail, "love", "text status", model_textStatus.getUseremail());

                                if (currentFlag.equals("love")) objDocumentReference.update("currentflag", "love");
                                else if (currentFlag.equals("haha")) {
                                    Long totalHearts = (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).get("nooflove");
                                    totalHearts++;

                                    getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition())
                                            .getReference().update("nooflove", totalHearts);

                                    objDocumentReference.update("currentflag", "love");
                                    Long totalHaha = (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).get("noofhaha");
                                    totalHaha--;
                                    getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition())
                                            .getReference().update("noofhaha", totalHaha);
                                } else if (currentFlag.equals("sad")) {
                                    Long totalHearts = (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).get("nooflove");
                                    totalHearts++;

                                    getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition())
                                            .getReference().update("nooflove", totalHearts);

                                    objDocumentReference.update("currentflag", "love");
                                    Long totalSad = (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).get("noofsad");
                                    totalSad--;
                                    getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).getReference()
                                            .update("noofsad", totalSad);
                                }
                            } else {
                                Map<String, Object> objectMap = new HashMap<>();
                                objectMap.put("currentflag", "love");

                                objFirebaseFirestore.collection("TextStatus")
                                        .document(documentID).collection("Emotions")
                                        .document(userEmail)
                                        .set(objectMap);

                                Long totalHearts = (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).get("nooflove");
                                totalHearts++;

                                getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition())
                                        .getReference().update("nooflove", totalHearts);

                                objDocumentReference.update("currentflag", "love");
                                objectAddNotifications.generateNotification(userEmail, "love", "text status", model_textStatus.getUseremail());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(textStatusViewHolder.heartIV.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else Toast.makeText(textStatusViewHolder.heartIV.getContext(), "Giriş yapmış bir kullanıcı yok", Toast.LENGTH_SHORT).show();
            }
        });

        textStatusViewHolder.hahaIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth objFirebaseAuth = FirebaseAuth.getInstance();

                if (objFirebaseAuth != null) {
                    String userEmail = objFirebaseAuth.getCurrentUser().getEmail();
                    String documentID = getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).getId();

                    FirebaseFirestore objFirebaseFirestore = FirebaseFirestore.getInstance();
                    DocumentReference objDocumentReference = objFirebaseFirestore.collection("TextStatus")
                            .document(documentID).collection("Emotions")
                            .document(userEmail);
                    objDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult().exists()) {
                                String currentFlag = task.getResult().getString("currentflag");
                                objectAddNotifications.generateNotification(userEmail, "haha", "text status", model_textStatus.getUseremail());

                                if (currentFlag.equals("haha")) objDocumentReference.update("currentflag", "haha");
                                else if (currentFlag.equals("love")) {
                                    Long totalHaha = (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).get("noofhaha");
                                    totalHaha++;

                                    getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition())
                                            .getReference().update("noofhaha", totalHaha);

                                    objDocumentReference.update("currentflag", "haha");
                                    Long totalLove = (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).get("nooflove");
                                    totalLove--;
                                    getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition())
                                            .getReference().update("nooflove", totalLove);
                                } else if (currentFlag.equals("sad")) {
                                    Long totalHaha = (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).get("noofhaha");
                                    totalHaha++;

                                    getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition())
                                            .getReference().update("noofhaha", totalHaha);

                                    objDocumentReference.update("currentflag", "haha");
                                    Long totalSad = (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).get("noofsad");
                                    totalSad--;
                                    getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).getReference()
                                            .update("noofsad", totalSad);
                                }
                            } else {
                                Map<String, Object> objectMap = new HashMap<>();
                                objectMap.put("currentflag", "haha");

                                objFirebaseFirestore.collection("TextStatus")
                                        .document(documentID).collection("Emotions")
                                        .document(userEmail)
                                        .set(objectMap);

                                Long totalHaha = (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).get("noofhaha");
                                totalHaha++;

                                getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition())
                                        .getReference().update("noofhaha", totalHaha);

                                objDocumentReference.update("currentflag", "haha");
                                objectAddNotifications.generateNotification(userEmail, "haha", "text status", model_textStatus.getUseremail());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(textStatusViewHolder.hahaIV.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else Toast.makeText(textStatusViewHolder.hahaIV.getContext(), "Giriş yapmış bir kullanıcı yok", Toast.LENGTH_SHORT).show();
            }
        });

        textStatusViewHolder.sadIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth objFirebaseAuth = FirebaseAuth.getInstance();

                if (objFirebaseAuth != null) {
                    String userEmail = objFirebaseAuth.getCurrentUser().getEmail();
                    String documentID = getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).getId();

                    FirebaseFirestore objFirebaseFirestore = FirebaseFirestore.getInstance();
                    DocumentReference objDocumentReference = objFirebaseFirestore.collection("TextStatus")
                            .document(documentID).collection("Emotions")
                            .document(userEmail);
                    objDocumentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult().exists()) {
                                String currentFlag = task.getResult().getString("currentflag");
                                objectAddNotifications.generateNotification(userEmail, "sad", "text status", model_textStatus.getUseremail());

                                if (currentFlag.equals("sad")) objDocumentReference.update("currentflag", "sad");
                                else if (currentFlag.equals("love")) {
                                    Long totalSad = (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).get("noofsad");
                                    totalSad++;

                                    getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition())
                                            .getReference().update("noofsad", totalSad);

                                    objDocumentReference.update("currentflag", "sad");
                                    Long totalLove = (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).get("nooflove");
                                    totalLove--;
                                    getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition())
                                            .getReference().update("nooflove", totalLove);
                                } else if (currentFlag.equals("haha")) {
                                    Long totalSad = (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).get("noofsad");
                                    totalSad++;

                                    getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition())
                                            .getReference().update("noofsad", totalSad);

                                    objDocumentReference.update("currentflag", "sad");
                                    Long totalHaha = (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).get("noofhaha");
                                    totalHaha--;
                                    getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).getReference()
                                            .update("noofhaha", totalHaha);
                                }
                            } else {
                                Map<String, Object> objectMap = new HashMap<>();
                                objectMap.put("currentflag", "sad");

                                objFirebaseFirestore.collection("TextStatus")
                                        .document(documentID).collection("Emotions")
                                        .document(userEmail)
                                        .set(objectMap);

                                Long totalHaha = (Long) getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).get("noofsad");
                                totalHaha++;

                                getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition())
                                        .getReference().update("noofsad", totalHaha);

                                objDocumentReference.update("currentflag", "sad");
                                objectAddNotifications.generateNotification(userEmail, "sad", "text status", model_textStatus.getUseremail());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(textStatusViewHolder.sadIV.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else Toast.makeText(textStatusViewHolder.sadIV.getContext(), "Giriş yapmış bir kullanıcı yok", Toast.LENGTH_SHORT).show();
            }
        });

        textStatusViewHolder.commentIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String documentID = getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).getId();
                Context objectContext = textStatusViewHolder.commentIV.getContext();
                Intent objectIntent = new Intent(objectContext, TextCommentPage.class);
                objectIntent.putExtra("documentId", documentID);
                objectIntent.putExtra("userEmailID", model_textStatus.getUseremail());
                objectContext.startActivity(objectIntent);
            }
        });

        textStatusViewHolder.favoriteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String documentId = getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).getId();
                FirebaseFirestore objectFirebaseFirestore = FirebaseFirestore.getInstance();

                DocumentReference objectDocumentReference = objectFirebaseFirestore.collection("TextStatus")
                        .document(documentId);
                objectDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String userEmail = documentSnapshot.getString("useremail");
                        String status = documentSnapshot.getString("status");
                        String profileUrl = documentSnapshot.getString("profileurl");
                        String statusDate = documentSnapshot.getString("currentdatetime");

                        Map<String, Object> objectMap = new HashMap<>();
                        objectMap.put("useremail", userEmail);
                        objectMap.put("status", status);
                        objectMap.put("profileurl", profileUrl);
                        objectMap.put("currentdatetime", statusDate);

                        FirebaseAuth objectFirebaseAuth = FirebaseAuth.getInstance();
                        if (objectFirebaseAuth != null) {
                            String currentLoggedInUser = objectFirebaseAuth.getCurrentUser().getEmail();
                            objectFirebaseFirestore.collection("UserFavorite").document(currentLoggedInUser)
                                    .collection("FavoriteTextStatus")
                                    .document(documentId)
                                    .set(objectMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(textStatusViewHolder.favoriteIV.getContext(), "Favoriye eklendi", Toast.LENGTH_SHORT).show();
                                    objectAddNotifications.generateNotification(userEmail, "favorite", "text status", model_textStatus.getUseremail());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(textStatusViewHolder.favoriteIV.getContext(), "Favoriye eklenemedi", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else Toast.makeText(textStatusViewHolder.favoriteIV.getContext(), "Giriş yapmış bir kullanıcı yok", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });

        textStatusViewHolder.deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth objectFirebaseAuth = FirebaseAuth.getInstance();
                if (objectFirebaseAuth.getCurrentUser().getEmail().equals(model_textStatus.getUseremail())) {
                    FirebaseFirestore objectFirebaseFirestore = FirebaseFirestore.getInstance();
                    objectFirebaseFirestore.collection("TextStatus")
                            .document(getSnapshots().getSnapshot(textStatusViewHolder.getAdapterPosition()).getId())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(textStatusViewHolder.deleteIV.getContext(), "Statü silindi", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(textStatusViewHolder.deleteIV.getContext(), "Statü silinemedi", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else Toast.makeText(textStatusViewHolder.deleteIV.getContext(), "Bu statüyü silme yetkiniz yok", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public TextStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TextStatusViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.model_text_status, parent, false
        ));
    }

    public class TextStatusViewHolder extends RecyclerView.ViewHolder {
        ImageView profileIV;
        ImageView heartIV, hahaIV, sadIV, deleteIV, commentIV, favoriteIV;
        TextView userEmailTV, dateTimeTV, userStatusTV, heartCountTV, hahaCountTV, sadCountTV, commentCountTV;

        public TextStatusViewHolder(@NonNull View itemView) {
            super(itemView);
            profileIV = itemView.findViewById(R.id.model_textStatus_profileIV);
            heartIV = itemView.findViewById(R.id.model_textStatus_heartIV);
            hahaIV = itemView.findViewById(R.id.model_textStatus_hahaIV);
            sadIV = itemView.findViewById(R.id.model_textStatus_sadIV);
            deleteIV = itemView.findViewById(R.id.model_textStatus_deleteIV);
            commentIV = itemView.findViewById(R.id.model_textStatus_commentIV);
            favoriteIV = itemView.findViewById(R.id.model_textStatus_favoriteTextStatus);
            userEmailTV = itemView.findViewById(R.id.model_textStatus_emailTV);
            dateTimeTV = itemView.findViewById(R.id.model_textStatus_dateTV);
            userStatusTV = itemView.findViewById(R.id.model_textStatus_textStatusTV);
            heartCountTV = itemView.findViewById(R.id.model_textStatus_heartCountTV);
            hahaCountTV = itemView.findViewById(R.id.model_textStatus_hahaCountTV);
            sadCountTV = itemView.findViewById(R.id.model_textStatus_sadCountTV);
            commentCountTV = itemView.findViewById(R.id.model_textStatus_commentCountTV);
        }
    }
}
