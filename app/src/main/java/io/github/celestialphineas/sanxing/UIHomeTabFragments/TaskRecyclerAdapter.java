package io.github.celestialphineas.sanxing.UIHomeTabFragments;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.celestialphineas.sanxing.R;
import io.github.celestialphineas.sanxing.sxObject.Task;


/**
 * Created by celestialphineas on 17-10-17.
 * Public class recycler view adapter for the task fragment
 */

public class TaskRecyclerAdapter
        extends RecyclerView.Adapter<TaskRecyclerAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private Context context;
    private Calendar taskCalendar = Calendar.getInstance();

    TaskRecyclerAdapter() { }
    TaskRecyclerAdapter(List<Task> task_list) {
        taskList = task_list;
    }

    public List<Task> getTaskList() {
        return taskList;
    }
    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_headline)       TextView taskTitle;
        @BindView(R.id.card_subheading)     TextView taskDueTime;
        @BindView(R.id.card_countdown)      TextView taskCountdown;
        @BindView(R.id.card_description)    TextView taskDescription;
        @BindView(R.id.card_button_edit)    AppCompatButton buttonEdit;
        @BindView(R.id.card_button_delete)  AppCompatButton buttonDelete;
        TaskViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewGroup) {
        View itemView = LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.home_card, parent, false);
        context = parent.getContext();
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TaskViewHolder holder, final int position) {
        // TODO: Set taskCalendar to the due calendar got from the database
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);
        String timeString = dateFormat.format(taskCalendar.getTime())
                + " " + timeFormat.format(taskCalendar.getTime());
        holder.taskTitle.setText(taskList.get(position).getContent());
        holder.taskDueTime.setText(timeString);

        // TODO: Calculate the time difference (i.e. the countdown) and print it here
        holder.taskCountdown.setText("3 Days 11:23");
        // TODO: Get the description and print it out here
        holder.taskDescription.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Curabitur non mauris lorem. Mauris ac ex nec purus feugiat venenatis. Suspendisse fringilla.");

        // Button edit behavior
        holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Create a new activity for editing
            }
        });
        // Button delete behavior
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Task task = taskList.get(position);
                remove(position);
                View.OnClickListener redo = new View.OnClickListener() {
                    @Override public void onClick(View view) {
                        add(task, position);
                        // TODO: Resume the lazily deleted database entry
                    }
                };
                Snackbar.make(view, R.string.snack_one_item_deleted, R.integer.undo_timeout)
                        .setAction(R.string.undo, redo)
                        .show();
                // TODO: Lazy delete a database entry
            }
        });
        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return taskList == null ? 0: taskList.size();
    }

    // Remove an entry
    public void remove(int position) {
        try { taskList.remove(position); } catch (Exception e) {
            Log.e("TaskRecyclerAdapter", "remove: " + position + ", " + taskList.size());
        }
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, taskList.size());
    }

    // Add an entry
    public void add(Task task, int position) {
        try { taskList.add(position, task); } catch (Exception e) {
            Log.e("TaskRecyclerAdapter", "add: " + position + ", " + taskList.size());
        }
        notifyItemInserted(position);
        notifyItemRangeChanged(position, taskList.size());
    }
}
