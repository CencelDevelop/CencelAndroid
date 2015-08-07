/* Copyright (c) 2009 Matthias Käppler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mx.com.cencel.comercial.cencel.activities.promos;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Gallery.LayoutParams;
import com.androidquery.AQuery;
import mx.com.cencel.comercial.cencel.util.CencelUtils;

/**
 * 
 * @author Matthias Kaeppler
 */
public class RemoteImageGalleryAdapter extends BaseAdapter {

    private List<String> imageUrls;

    private Context context;

    private Drawable progressDrawable, errorDrawable;
    
    private Drawable defaultDrawable;
    
    private AQuery aq;

    public RemoteImageGalleryAdapter(Context context) {
        this(context, null, null, null, null);
    }

    /**
     * @param context
     *            the current context
     * @param imageUrls
     *            the set of image URLs which are to be loaded and displayed
     */
    public RemoteImageGalleryAdapter(Context context, List<String> imageUrls) {
        this(context, imageUrls, null, null, null);
    }

    /**
     * @param context
     *            the current context
     * @param imageUrls
     *            the set of image URLs which are to be loaded and displayed
     * @param progressDrawable
     *            the drawable that will be used for rendering progress
     * @param errorDrawable
     *            the drawable that will be used if a download error occurs
     */
    public RemoteImageGalleryAdapter(Context context, List<String> imageUrls, Drawable progressDrawable,
            Drawable errorDrawable, Drawable defaultDrawable) {
        this.imageUrls = imageUrls;
        this.context = context;
        this.progressDrawable = progressDrawable;
        this.errorDrawable = errorDrawable;
        this.defaultDrawable = defaultDrawable;
    }
    
    private AQuery getAQuery() {
    	if(aq == null) {
    		aq = new AQuery(context);
    	}
    	return aq;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setProgressDrawable(Drawable progressDrawable) {
        this.progressDrawable = progressDrawable;
    }

    public Drawable getProgressDrawable() {
        return progressDrawable;
    }

    public void setErrorDrawable(Drawable errorDrawable) {
        this.errorDrawable = errorDrawable;
    }

    public Drawable getErrorDrawable() {
        return errorDrawable;
    }

    // TODO: both convertView and ViewHolder are pointless at the moment, since there's a framework
    // bug which causes views to not be cached in a Gallery widget:
    // http://code.google.com/p/android/issues/detail?id=3376
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String imageUrl = (String) getItem(position);

        ViewHolder viewHolder = null;
        ImageView remoteImageView = null;

        if (convertView == null) {
            // create the image view
            remoteImageView = new ImageView(context);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.FILL_PARENT);
            lp.gravity = Gravity.CENTER;
            remoteImageView.setLayoutParams(lp);

            // create the container layout for the image view
            FrameLayout container = new FrameLayout(context);
            container.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.FILL_PARENT));
            container.addView(remoteImageView, 0);

            convertView = container;

            viewHolder = new ViewHolder();
            viewHolder.webImageView = remoteImageView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            remoteImageView = viewHolder.webImageView;
        }

        // calling reset is important to prevent old images from displaying in a recycled view.
//        remoteImageView.reset();
//        remoteImageView.setImageUrl(imageUrl);
//        remoteImageView.loadImage();
        
        if(CencelUtils.isBlank(imageUrl)) {
        	remoteImageView.setImageDrawable(defaultDrawable);
        } else {
        	getAQuery().id(remoteImageView).image(imageUrl, true, true, 400, 0);
        }

        onGetView(position, remoteImageView, (ViewGroup) convertView, parent);

        return convertView;
    }

    protected void onGetView(int position, ImageView remoteImageView,
            ViewGroup remoteImageViewContainer, ViewGroup parent) {
        // for extension
    }

    private static final class ViewHolder {
        private ImageView webImageView;
    }
}
