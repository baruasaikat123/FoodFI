package com.example.foodsi.ProjectAdapter;
import com.example.foodsi.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;


public class RecyclerSubCategoryAdapter extends RecyclerView.Adapter<RecyclerSubCategoryAdapter.ViewHolder> {

    private ArrayList<String> subCategoryList;
    private ItemClickListener itemClickListener;

    public RecyclerSubCategoryAdapter(ArrayList<String> subCategoryList, ItemClickListener itemClickListener){
        this.subCategoryList = subCategoryList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_content_sub_category, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.subCategoryName.setText(subCategoryList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(subCategoryList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() { return subCategoryList.size(); }

    public interface ItemClickListener{ void onItemClick(String subCategoryName); }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView subCategoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subCategoryName = (TextView) itemView.findViewById(R.id.sub_category_name);
        }
    }
}
