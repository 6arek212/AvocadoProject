package com.example.testavocado.Home.adapters;

import android.app.AlertDialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.testavocado.Dialogs.CommentMethodsHandler;
import com.example.testavocado.Models.Comment;
import com.example.testavocado.R;
import com.example.testavocado.Utils.HelpMethods;
import com.example.testavocado.Utils.TimeMethods;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {
    private static final String TAG = "CommentsAdapter";

    public interface OnDeletingCommentListener{
        void OnDeleteComment();
    }


    List<Comment> commentList =new ArrayList<Comment>();

    public int current_user_id,post_id;
    private Context mContext;
    public boolean is_user_Post;
    private OnDeletingCommentListener onDeletingCommentListener;


    public CommentsAdapter(Context context,OnDeletingCommentListener onDeletingCommentListener) {
        this.mContext=context;
        this.current_user_id = HelpMethods.checkSharedPreferencesForUserId(context);
        this.onDeletingCommentListener=onDeletingCommentListener;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_comment_item, viewGroup, false);
        return new CommentViewHolder(view);
    }



    /**
     *      attaching the data
     *
     */

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder commentViewHolder, int i) {
        commentViewHolder.mCommentUserName.setText(commentList.get(i).getComment_user_name());
        commentViewHolder.mCommentText.setText(commentList.get(i).getComment_text());
        commentViewHolder.mCommentTime.setText(TimeMethods.getTimestampDifference(commentList.get(i).getComment_date_time()));

        Glide.with(mContext)
                .asBitmap()
                .load(commentList.get(i).getComment_user_profile_image_path())
                .centerCrop()
                .apply(
                        new RequestOptions()
                                .placeholder(R.drawable.loading_img)
                                .error(R.drawable.error)
                )
                .into(commentViewHolder.mProfileImage);


        if(commentList.get(i).getComment_user_id()==current_user_id || is_user_Post){
            commentViewHolder.deleteComment();
        }else {
            commentViewHolder.mDeleteComment.setVisibility(View.GONE);
        }

    }












    public void addSetOfNewComment(List<Comment> comments,int s) {
        commentList.addAll(comments);
        notifyItemRangeInserted(s,comments.size());
    }


    public void addSetOfNewCommentTop(List<Comment> comments) {
        commentList.addAll(0,comments);
        notifyItemRangeInserted(0,comments.size());
    }



    /**
     *      clear the recycler view list
     *
     */
    public void clearList() {
        Log.d(TAG, "clearList: ");
        commentList.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return commentList.size();
    }




    public class CommentViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mProfileImage;
        TextView mCommentUserName, mCommentText, mCommentTime,mDeleteComment;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            mCommentUserName = itemView.findViewById(R.id.commentUserName);
            mCommentText = itemView.findViewById(R.id.commentText);
            mCommentTime = itemView.findViewById(R.id.commentTime);
            mProfileImage=itemView.findViewById(R.id.profileImage);
            mDeleteComment=itemView.findViewById(R.id.deleteComment);
        }

        public void deleteComment() {
            mDeleteComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert=new AlertDialog.Builder(mContext,R.style.AlertDialogStyle2);
                    alert.setTitle("Are you sure for deleting the comment for "+commentList.get(getAdapterPosition()).getComment_user_name());
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CommentMethodsHandler.deleteComment(commentList.get(getAdapterPosition()).getComment_id(),post_id, new CommentMethodsHandler.OnDeletingCommentListener() {
                                @Override
                                public void OnDeleted() {
                                    commentList.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());
                                    onDeletingCommentListener.OnDeleteComment();
                                }

                                @Override
                                public void onServerException(String ex) {
                                    Log.d(TAG, "onServerException: "+ex);

                                }

                                @Override
                                public void onFailureListener(String ex) {
                                    Log.d(TAG, "onFailureListener: "+ex);

                                }
                            });
                        }
                    });

                    alert.setNegativeButton("Cancel",null);
                    alert.show();
                }
            });
        }
    }


}
