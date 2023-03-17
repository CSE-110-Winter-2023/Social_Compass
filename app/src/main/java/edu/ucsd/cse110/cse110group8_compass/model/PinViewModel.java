package edu.ucsd.cse110.cse110group8_compass.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.cse110group8_compass.Pin;

public class PinViewModel extends AndroidViewModel {
    private LiveData<List<UUID>> uuids;
    public UUIDRepository repo;


    public PinViewModel(@NonNull Application application) {
        super(application);
        var context = application.getApplicationContext();
        var db = UUIDDatabase.provide(context);
        var dao = db.getDao();
        this.repo = new UUIDRepository(dao);
    }


    /**
     * Load all notes from the database.
     * Load all notes from the database.
     * @return a LiveData object that will be updated when any notes change.
     */
    public LiveData<List<UUID>> getUUID() {
        if (uuids == null) {
            uuids = repo.getAllLocal(); //should just return from getSynced()
        }
        return uuids;
    }


    public LiveData<UUID> getUUIDFromRemote(String public_code) {
        return repo.getRemote(public_code);
    }



    /**
     * Open a note in the database. If the note does not exist, create it.
     * @param publicId the title of the note
     * @return a LiveData object that will be updated when this note changes.
     */
    public LiveData<UUID> getOrCreateUUID(String publicId) {
        Log.i("PinViewModel", "public ID: "+ repo.existsLocal(publicId));
        if (!repo.existsLocal(publicId)) {
            var uuid = new UUID("null", 0.0, 0.0, publicId);
            repo.upsertLocal(uuid, false);
        }

        return repo.getLocal(publicId);
    }

    public void delete(UUID uuid) {
        repo.deleteLocal(uuid);
    }
}
