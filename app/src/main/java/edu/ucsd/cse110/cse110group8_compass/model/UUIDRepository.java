package edu.ucsd.cse110.cse110group8_compass.model;

import android.os.StrictMode;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class UUIDRepository {

    private final UUIDDao dao;
    private ScheduledFuture<?> poller; // what could this be for... hmm?
    UUIDAPI api = new UUIDAPI();


    public UUIDRepository(UUIDDao dao) {
        this.dao = dao;
    }

    // Synced Methods
    // ==============

    /**
     * This is where the magic happens. This method will return a LiveData object that will be
     * updated when the note is updated either locally or remotely on the server. Our activities
     * however will only need to observe this one LiveData object, and don't need to care where
     * it comes from!
     * <p>
     * This method will always prefer the newest version of the note.
     *
     * @param public_code the title of the note
     * @return a LiveData object that will be updated when the note is updated locally or remotely.
     */
    public LiveData<UUID> getSynced(String public_code) {
        var pin = new MediatorLiveData<UUID>();

        Observer<UUID> updateFromRemote = theirPin -> {
            var ourPin = pin.getValue();

            // Log.i("UpdateFromRemote", "our note: " + ourNote.version);
            // Log.i("UpdateFromRemote", "remote note: " + theirNote.version);

            if (theirPin == null) return; // do nothing
            if (ourPin == null || ourPin.updatedAt < theirPin.updatedAt) {
                upsertLocal(theirPin, false);
            }
        };

        // If we get a local update, pass it on.
        pin.addSource(getLocal(public_code), pin::postValue);
        // If we get a remote update, update the local version (triggering the above observer)
        pin.addSource(getRemote(public_code), updateFromRemote);

        return pin;
    }

    public void upsertSynced(UUID pin) {
        upsertLocal(pin);
        upsertRemote(pin);
    }

    // Local Methods
    // =============

    public LiveData<UUID> getLocal(String public_code) {
        return dao.get(public_code);
    }

    public LiveData<List<UUID>> getAllLocal() {
        return dao.getAll();
    }

    public void upsertLocal(UUID note, boolean incrementVersion) {
        // We don't want to increment when we sync from the server, just when we save.
        if (incrementVersion) note.updatedAt = note.updatedAt + 1;
        dao.upsert(note);
    }

    public void upsertLocal(UUID note) {
        upsertLocal(note, true);
    }

    public void deleteLocal(UUID note) {
        dao.delete(note);
    }

    public boolean existsLocal(String title) {
        return dao.exists(title);
    }

    // Remote Methods
    // ==============

    public LiveData<UUID> getRemote(String public_code) {
        // TODO: Implement getRemote!
        // TODO: Set up polling background thread (MutableLiveData?)
        // TODO: Refer to TimerService from https://github.com/DylanLukes/CSE-110-WI23-Demo5-V2.

        // Cancel any previous poller if it exists.
        if (this.poller != null && !this.poller.isCancelled()) {
            poller.cancel(true);
        }

        // Start by fetching the note from the server _once_ and feeding it into MutableLiveData.

        Log.i("Trying response", public_code);
        // var executor = Executors.newSingleThreadScheduledExecutor();
        MutableLiveData<UUID> realTimeData = new MutableLiveData<>();
        var executor = Executors.newSingleThreadScheduledExecutor();
        poller = executor.scheduleAtFixedRate(() -> {
            UUID n = api.get(public_code);
            realTimeData.postValue(n);
        }, 0, 3000, TimeUnit.MILLISECONDS);
        MediatorLiveData<UUID> noteData = new MediatorLiveData<>();
        noteData.addSource(realTimeData, noteData::postValue);

        // Start by fetching the note from the server ONCE.
        // Then, set up a background thread that will poll the server every 3 seconds.

        // You may (but don't have to) want to cache the LiveData's for each public_code, so that
        // you don't create a new polling thread every time you call getRemote with the same public_code.
        // You don't need to worry about killing background threads.

        // throw new UnsupportedOperationException("Not implemented yet");
        return noteData;
    }

    public void upsertRemote(UUID note) {
        // TODO: Implement upsertRemote!
        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);
        api.put(note);

        /*
        var thread = executor.scheduleAtFixedRate(() -> {
            api.put(note);
        }, 0, 3000, TimeUnit.MILLISECONDS);
        if (thread != null && !thread.isCancelled()) {
            thread.cancel(true);
        }
        */
        // throw new UnsupportedOperationException("Not implemented yet");
    }
}
