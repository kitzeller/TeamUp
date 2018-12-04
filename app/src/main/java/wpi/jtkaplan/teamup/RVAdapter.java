package wpi.jtkaplan.teamup;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import wpi.jtkaplan.teamup.model.Class;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ClassViewHolder> {

    public static class ClassViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView className;
        TextView classID;

        ClassViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);

            className = (TextView) itemView.findViewById(R.id.class_name);
            classID = (TextView) itemView.findViewById(R.id.class_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast toast = Toast.makeText(view.getContext(),
                            "You clicked class " + classID.getText(),
                            Toast.LENGTH_SHORT);

                    toast.show();

                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    HomeFragment myFragment = new HomeFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();

                }
            });
        }

        public void setColor(int c){
            cv.setBackgroundResource(c);
        }
    }

    List<Class> classes;

    RVAdapter(List<Class> classes) {
        this.classes = classes;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ClassViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        ClassViewHolder cvh = new ClassViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(ClassViewHolder classViewHolder, int i) {
        classViewHolder.className.setText(classes.get(i).getName());
        classViewHolder.classID.setText(classes.get(i).getId());

        // i refers to class index in classes list
        //System.out.println(i);
        int color = R.color.LIGHTGRAY;
        switch (i){
            case 0:
                color = R.color.LIGHTCORAL;
                break;
            case 1:
                color = R.color.LIGHTBLUE;
                break;
            case 3:
                color = R.color.LIGHTGREEN;
        }


        classViewHolder.setColor(color);
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }
}