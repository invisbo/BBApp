package tr.com.bbapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import tr.com.bbapp.R;
import tr.com.bbapp.network.model.ListItem;

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.ItemHolder> {
    private List<ListItem> items = new ArrayList<>();

    @NonNull
    @Override
    public MainListAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainListAdapter.ItemHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull MainListAdapter.ItemHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<ListItem> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void updateItem(ListItem item) {
        if (item == null) return;
        ListIterator<ListItem> iterator = items.listIterator();
        while (iterator.hasNext()) {
            ListItem i = iterator.next();
            if (i.getId() == item.getId()) {
                iterator.set(item);
                notifyDataSetChanged();
                break;
            }
        }
    }


    public class ItemHolder extends RecyclerView.ViewHolder {

        ItemHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_list, parent, false));
        }

        void bind() {
            ((TextView) itemView).setText(items.get(getAdapterPosition()).getName());
        }
    }
}
