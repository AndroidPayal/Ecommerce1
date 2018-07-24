package payal.cluebix.www.ecommerce.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import payal.cluebix.www.ecommerce.Datas.CategoryTypeData;
import payal.cluebix.www.ecommerce.FilterResultActivity;
import payal.cluebix.www.ecommerce.R;



    public class CategoryTypeAdapter extends RecyclerView.Adapter<CategoryTypeAdapter.ViewHolder> {


        private Context context;
        //    private List<ListItemQuote> listItems;
        private List<CategoryTypeData> listItems;

        public CategoryTypeAdapter(Context context, List listItems) {
            this.context = context;
            this.listItems = listItems;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_type_adapter,parent,false);



            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CategoryTypeAdapter.ViewHolder holder, int position) {

//        ListItemQuote item = listItems.get(position);

            CategoryTypeData item = listItems.get(position);

            holder.categoryTitle.setText(item.getCategoryName());


            holder.categoryIcon.setText(""+item.getCategoryName().charAt(0));

            holder.categoryIcon.setBackgroundResource(item.getDrawableId());




//            holder.categoryIcon.setText(item.getCategoryName().charAt(0));

//            holder.productName.setText(item.getProductName());
//            holder.productCode.setText(item.getProductCode());
//            holder.productPrize.setText(item.getProductPrize());

//        holder.name.setText(item.getName());
//        holder.description.setText(item.getDescription());
//        holder.rating.setText(item.getRating());
        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView categoryTitle;
            public TextView categoryIcon;



            public ViewHolder(View itemView) {

                super(itemView);

                categoryTitle = (TextView) itemView.findViewById(R.id.category_type_title);
                categoryIcon = (TextView) itemView.findViewById(R.id.category_typetext);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int position = getAdapterPosition();

                        CategoryTypeData items = listItems.get(position);

                        Intent intent = new Intent(context, FilterResultActivity.class);

                        intent.putExtra("categoryType", items.getCategoryName());

                        intent.putExtra("spinnerPosition",position);

                        Log.d("Anonymous",""+items.getCategoryName());

                        context.startActivity(intent);
                    }
                });

//                categoryIcon = (TextView) itemView.findViewById(R.id.category_type_textIcon);
            }
        }
    }

