package edu.uw.tcss450.howlr.io;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.collection.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Singleton class for handling requests.
 */
public class RequestQueueSingleton {

    /**
     * The instance of the request.
     */
    private static RequestQueueSingleton instance;

    /**
     * The context of the request.
     */
    private static Context context;

    /**
     * The queue of requests.
     */
    private RequestQueue mRequestQueue;

    /**
     * The image loader.
     */
    private ImageLoader mImageLoader;

    /**
     * Creates a queue of requests created from a context.
     * @param context The context
     */
    private RequestQueueSingleton(Context context) {
        RequestQueueSingleton.context = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    /**
     * Gets the instance of the singleton queue from the context.
     * @param context The context
     * @return The singleton queue
     */
    public static synchronized RequestQueueSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new RequestQueueSingleton(context);
        }
        return instance;
    }

    /**
     * Gets the queue of requests.
     * @return The queue of requests.
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * Adds the list of requests to the queue of requests.
     * @param req The list of requests
     * @param <T> The type of request
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    /**
     * Gets the image loader.
     * @return The image loader
     */
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}