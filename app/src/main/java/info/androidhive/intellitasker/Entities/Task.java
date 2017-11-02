package info.androidhive.intellitasker.Entities;

import java.util.Comparator;
import java.util.Date;


public class Task {
    public Date startTime;
    public Date endTime;

    public Task(Date st, Date en) {
        startTime = st;
        endTime = en;
    }

    Task() {

    }

    public static Comparator<Task> TaskSorter = new Comparator<Task>() {

        public int compare(Task t1, Task t2) {

            Date TaskStart1 = t1.startTime;
            Date TaskStart2 = t2.startTime;


            return TaskStart1.compareTo(TaskStart2);

        }
    };
}
