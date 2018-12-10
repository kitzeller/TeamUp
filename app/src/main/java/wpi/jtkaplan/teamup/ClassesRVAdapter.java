package wpi.jtkaplan.teamup;

import android.support.annotation.NonNull;
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

public class ClassesRVAdapter extends RecyclerView.Adapter<ClassesRVAdapter.ClassViewHolder> {

    // Dummmy class (doesn't get added to db)
    private final static Class addClassClass = new Class("Add A New Class!");

    List<Class> classes;

    ClassesRVAdapter(List<Class> classes) {
        this.classes = classes;
        this.classes.add(addClassClass);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder classViewHolder, int i) {
        classViewHolder.className.setText(classes.get(i).getName());
        classViewHolder.classID.setText(classes.get(i).getId());
        classViewHolder.setClassObject(classes.get(i));

        // i refers to class index in classes list
        //System.out.println(i);
        int color = R.color.LIGHTGRAY;
        switch (i) {
            case 0:
                color = R.color.LIGHTCORAL;
                break;
            case 1:
                color = R.color.LIGHTBLUE;
                break;
            case 3:
                color = R.color.LIGHTGREEN;
        }

        if (i == classes.indexOf(addClassClass)) {
            classViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast toast = Toast.makeText(view.getContext(),
                            "Adding Class...",
                            Toast.LENGTH_SHORT);

                    toast.show();
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();

                    if (UserPreferences.read(UserPreferences.LOC_VALUE,null).equals("Students")){
                        System.out.println("Students");
                        AddClassFragment myFragment = new AddClassFragment();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();

                    } else if (UserPreferences.read(UserPreferences.LOC_VALUE,null).equals("Professors")){
                        System.out.println("Professors");
                        CreateClassFragment myFragment = new CreateClassFragment();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();

                    }

                }
            });
        }

        classViewHolder.setColor(color);
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

    public void addClass(Class c){
        classes.add(c);
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView className;
        TextView classID;
        Class classObject;

        ClassViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);

            className = itemView.findViewById(R.id.class_name);
            classID = itemView.findViewById(R.id.class_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast toast = Toast.makeText(view.getContext(),
                            "You clicked class " + classID.getText(),
                            Toast.LENGTH_SHORT);

                    toast.show();

                    // Set selected Class
                    UserPreferences.setSelectedClass(classObject);

                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    GroupRecyclerViewFragment memberViewFragment = new GroupRecyclerViewFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, memberViewFragment).addToBackStack(null).commit();

                }
            });
        }

        public void setColor(int c) {
            cv.setCardBackgroundColor(cv.getResources().getColor(c));
        }

        public Class getClassObject() {
            return classObject;
        }

        public void setClassObject(Class classObject) {
            this.classObject = classObject;
        }
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }
}