package com.wizeline.omarsanchez.nytimessearch.adapters;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.wizeline.omarsanchez.nytimessearch.R;
import com.wizeline.omarsanchez.nytimessearch.databinding.ItemDocsBinding;
import com.wizeline.omarsanchez.nytimessearch.models.Docs;
import com.wizeline.omarsanchez.nytimessearch.models.Multimedia;

import java.util.List;

/**
 * Created by omarsanchez on 3/16/17.
 */

public class DocsAdapter extends RecyclerView.Adapter<DocsAdapter.DocsHolder> {
    List<Docs> docs;

    public DocsAdapter(List<Docs> docs) {
        this.docs = docs;
    }

    @Override
    public DocsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemDocsBinding binding = ItemDocsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DocsHolder(binding);
    }

    @Override
    public void onBindViewHolder(DocsHolder holder, int position) {
        final Docs doc = docs.get(position);
        holder.binding.setItem(doc);
        holder.binding.imageItem.setImageResource(R.drawable.ic_image_black_24dp);
        for (Multimedia multimedia : doc.getMultimedia()) {
            if (multimedia.getSubtype() != null) {
                Glide.with(holder.binding.getRoot().getContext()).load(multimedia.getUrl()).into(holder.binding.imageItem);
                break;
            }
        }
        holder.binding.cardItem.setOnClickListener(v -> {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary));
            builder.setActionButton(BitmapFactory.decodeResource(v.getResources(), R.drawable.ic_action_name),
                    "Share Link", getPendingIntent(v.getContext(), doc.getWebUrl()), true);

            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(v.getContext(), Uri.parse(doc.getWebUrl()));
        });
    }

    @Override
    public int getItemCount() {
        return docs.size();
    }

    public void newData(List<Docs> docs) {
        this.docs.clear();
        this.docs.addAll(docs);
        notifyDataSetChanged();
    }

    public void addData(List<Docs> docs) {
        int start = getItemCount();
        this.docs.addAll(docs);
        notifyItemRangeInserted(start, docs.size());
    }


    class DocsHolder extends RecyclerView.ViewHolder {
        final ItemDocsBinding binding;

        public DocsHolder(ItemDocsBinding itemDocsBinding) {
            super(itemDocsBinding.getRoot());
            this.binding = itemDocsBinding;
        }
    }

    public PendingIntent getPendingIntent(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, url);
        int requestCode = 100;

        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
}
