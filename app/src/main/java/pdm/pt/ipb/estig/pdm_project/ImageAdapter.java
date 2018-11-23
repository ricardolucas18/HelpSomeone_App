package pdm.pt.ipb.estig.pdm_project;

/**
 * Class ImageAdapter- This class is responsible for creating the view in feed activity
 *
 * @author Ricardo Lucas - nº 15297
 * @version 23/09/2018
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;



public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Upload> mUploads;
    public MenuItem delete;
    private FirebaseAuth firebaseAuth;

    private OnItemClickListener mListener;

    /**
     * Method ImageAdapter - Constructor.
     * @param context - context.
     * @param uploads - uploads.
     */
    public ImageAdapter(Context context, List<Upload> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    /**
     * Method ImageViewHolder - this method exucutes on create and put's an default image int posts while they are loading.
     * @param parent - parent.
     * @param viewType - viewType.
     * @return imageViewHolder - return a new view with default image.
     */
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);

        return new ImageViewHolder(v);
    }

    /**
     * Method onBindViewHolder- this method puts the information and image to holder in each position.
     * @param holder - holder.
     * @param position - position.
     */
    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Upload uploadCurrent = mUploads.get(position);
        holder.textViewTitle.setText(uploadCurrent.getTitle());
        holder.textViewDescription.setText(uploadCurrent.getDescription());
        holder.textViewCity.setText(uploadCurrent.getCity());
        holder.textViewCountry.setText(uploadCurrent.getCountry());
        holder.textViewDate.setText(uploadCurrent.getDate());
        Picasso.with(mContext)
                .load(uploadCurrent.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageView);

        //.centerInside
    }


    /**
     * Method getItemCount - this method returns number of uploads.
     * @return number of uploads.
     */
    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    /**
     * Class ImageAdapter- This class is responsible for creating each holder foi each upload.
     *
     */
    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{
        public TextView textViewTitle;
        public ImageView imageView;
        public TextView textViewDescription;
        public TextView textViewCity;
        public TextView textViewCountry;
        public TextView textViewDate;

        /**
         * Method ImageViewHolder - Constructor.
         * @param itemView - itemView.
         */
        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.text_view_title);

            imageView = itemView.findViewById(R.id.image_view_upload);

            textViewDescription = itemView.findViewById(R.id.text_view_description);

            textViewCity = itemView.findViewById(R.id.text_view_city);

            textViewCountry = itemView.findViewById(R.id.text_view_country);

            textViewDate = itemView.findViewById(R.id.text_view_date);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }

        /**
         * Method onClick -  this method it's executed when user clicks on a item.
         * @param view - view.
         */
        @Override
        public void onClick(View view) {
            if(mListener != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }

        /**
         * Method onCreateContextMenu -  this method it's executed on creating context menu.
         * @param contextMenu - contextMenu.
         * @param view - view.
         * @param contextMenuInfo - contextMenuInfo.
         */
        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select Action");
            MenuItem getLocation = contextMenu.add(ContextMenu.NONE, 1, 1, "Get Location");
            delete = contextMenu.add(ContextMenu.NONE, 2, 2, "Delete");

            getLocation.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        /**
         * Method onMenuItemClick -  this method it's executed when user clicks on a menu item, whitch can be get location or delete.
         * @param menuItem - menuItem.
         * @return true if user clicked on get location or delte, false if not.
         */
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if(mListener != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    switch (menuItem.getItemId()){
                        case 1:
                            mListener.onGetLocationClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);

        void onGetLocationClick(int position);

        void onDeleteClick(int position);

    }

    /**
     * Method setOnItemClickListener -  this method is the item click listener.
     * @param listener - listener.
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;

    }
}
