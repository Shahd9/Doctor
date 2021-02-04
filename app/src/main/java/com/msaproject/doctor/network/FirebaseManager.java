package com.msaproject.doctor.network;

import android.net.Uri;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.msaproject.doctor.Constants;
import com.msaproject.doctor.model.response.UploadStatusResponse;
import com.msaproject.doctor.utils.Optional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class FirebaseManager {

    private final FirebaseFirestore fireStoreInstance;
    private final FirebaseStorage firebaseStorage;
    private final StorageReference storageReference;

    @Inject
    public FirebaseManager() {
        fireStoreInstance = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    CollectionReference getCollectionReference(String collectionId) {
        return fireStoreInstance.collection(collectionId);
    }

    public void useCreatedAtServerTime(Map<String, Object> map) {
        map.put(Constants.MAP_KEY_CREATED_AT, FieldValue.serverTimestamp());
    }

    public void useUpdatedAtServerTime(Map<String, Object> map) {
        map.put(Constants.MAP_KEY_UPDATED_AT, FieldValue.serverTimestamp());
    }

    public <T> Single<Optional<T>> getDocumentSnapshot(String collectionId, String documentId, Class<T> parseClass) {
        Single<Optional<T>> observable = Single.create(emitter ->
                fireStoreInstance
                        .collection(collectionId)
                        .document(documentId)
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (emitter.isDisposed())
                                return;
                            emitter.onSuccess(Optional.ofNullable(documentSnapshot.toObject(parseClass)));
                        })
                        .addOnFailureListener(emitter::onError));
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public <T> Single<List<T>> getDocumentsInCollection(String collectionId, Class<T> parseClass) {
        Single<List<T>> observable = Single.create(emitter ->
                fireStoreInstance
                        .collection(collectionId)
                        .get()
                        .addOnCompleteListener(querySnapshotTask -> {
                            if (emitter.isDisposed())
                                return;
                            if (querySnapshotTask.isSuccessful())
                                emitter.onSuccess(Objects.requireNonNull(querySnapshotTask.getResult()).toObjects(parseClass));
                            else
                                emitter.onError(new NullPointerException());
                        }));
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public <T> Single<List<T>> getDocumentsByQuery(QueryBuilder queryBuilder, Class<T> parseClass) {
        Single<List<T>> observable = Single.create(emitter ->
                queryBuilder
                        .getQuerySnapshotTask()
                        .addOnCompleteListener(querySnapshotTask -> {
                            if (emitter.isDisposed())
                                return;
                            if (querySnapshotTask.isSuccessful())
                                emitter.onSuccess(Objects.requireNonNull(querySnapshotTask.getResult()).toObjects(parseClass));
                            else
                                emitter.onError(querySnapshotTask.getException());
                        }));
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable setValueToDocument(String collectionId, String documentId, Map<String, Object> objectHashMap) {
        Completable completable = Completable.create(emitter -> fireStoreInstance
                .collection(collectionId)
                .document(documentId)
                .set(objectHashMap)
                .addOnCompleteListener(task -> {
                    if (emitter.isDisposed())
                        return;
                    if (task.isSuccessful())
                        emitter.onComplete();
                    else
                        emitter.onError(Objects.requireNonNull(task.getException()));
                }));
        return completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable updateDocument(String collectionId, String documentId, Map<String, Object> objectHashMap) {
        Completable completable = Completable.create(emitter -> fireStoreInstance
                .collection(collectionId)
                .document(documentId)
                .update(objectHashMap)
                .addOnCompleteListener(task -> {
                    if (emitter.isDisposed())
                        return;
                    if (task.isSuccessful())
                        emitter.onComplete();
                    else
                        emitter.onError(Objects.requireNonNull(task.getException()));
                }));
        return completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable deleteDocument(String collectionId, String documentId) {
        Completable completable = Completable.create(emitter -> fireStoreInstance
                .collection(collectionId)
                .document(documentId)
                .delete()
                .addOnCompleteListener(task -> {
                    if (emitter.isDisposed())
                        return;
                    if (task.isSuccessful())
                        emitter.onComplete();
                    else
                        emitter.onError(Objects.requireNonNull(task.getException()));
                }));
        return completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<UploadStatusResponse> uploadFileWithProgressObservable(String folderId, String fileNameOnStorage, String extensionOnStorage, Uri photoUri) {
        Observable<UploadStatusResponse> observable = Observable.create(emitter ->
                storageReference
                        .child(folderId)
                        .child(fileNameOnStorage.concat(".").concat(extensionOnStorage))
                        .putFile(photoUri)
                        .addOnProgressListener(snapshot -> {
                            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                            emitter.onNext(new UploadStatusResponse(false, (int) progress, null));
                        })
                        .addOnCompleteListener(task -> {
                            if (emitter.isDisposed())
                                return;
                            if (task.isSuccessful()) {
                                Objects.requireNonNull(task.getResult()).getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                                    emitter.onNext(new UploadStatusResponse(true, 100, uri.toString()));
                                    emitter.onComplete();
                                }).addOnFailureListener(emitter::onError);
                            } else
                                emitter.onError(Objects.requireNonNull(task.getException()));
                        }));
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable deleteFileFromStorage(String fileDownloadUrl) {
        Completable completable = Completable.create(emitter -> firebaseStorage.getReferenceFromUrl(fileDownloadUrl).delete().addOnCompleteListener(task -> {
            if (emitter.isDisposed())
                return;
            if (task.isSuccessful())
                emitter.onComplete();
            else
                emitter.onError(task.getException());
        }));
        return completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
