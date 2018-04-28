package co.edu.ucc.todoapp.view.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.edu.ucc.todoapp.R;
import co.edu.ucc.todoapp.data.entidades.mapper.TaskEntityMapper;
import co.edu.ucc.todoapp.data.repository.task.TaskDataSourceFactory;
import co.edu.ucc.todoapp.data.repository.task.TaskRepository;
import co.edu.ucc.todoapp.domain.usecase.task.AddTaskUseCase;
import co.edu.ucc.todoapp.domain.usecase.task.GetAllTaskUseCase;
import co.edu.ucc.todoapp.view.adapters.TaskAdapter;
import co.edu.ucc.todoapp.view.presenter.IMainPresenter;
import co.edu.ucc.todoapp.view.presenter.MainPresenter;
import co.edu.ucc.todoapp.view.viewmodels.TaskViewModel;
import co.edu.ucc.todoapp.view.viewmodels.mapper.TaskViewModelMapper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements IMainView {

   // @BindView(R.id.ediTextDescrip)
   // EditText txtDescTask;


    @BindView(R.id.rvTask)
    RecyclerView rvTask;

    private IMainPresenter presenter;
    private TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        presenter = new MainPresenter(
                this,
                new AddTaskUseCase(
                        Schedulers.io(),
                        AndroidSchedulers.mainThread(),
                        new TaskRepository(
                                new TaskEntityMapper(),
                                new TaskDataSourceFactory()
                        )
                ),
                new GetAllTaskUseCase(
                        Schedulers.io(),
                        AndroidSchedulers.mainThread(),
                        new TaskRepository(
                                new TaskEntityMapper(),
                                new TaskDataSourceFactory()
                        )
                ),
                new TaskViewModelMapper()
        );

        initAdapter();
        initRecyclerView();
    }

    private void initAdapter() {
        taskAdapter = new TaskAdapter();
    }

    private void initRecyclerView() {
        rvTask.setLayoutManager(new LinearLayoutManager(this));
        rvTask.addItemDecoration(new android.support.v7.widget.DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        rvTask.setAdapter(taskAdapter);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessful() {
        Toast.makeText(this, R.string.msg_successful, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.fbAddTask)
    @Override
    public void addTask() {

        boolean wrapInScrollView = false;
        new MaterialDialog.Builder(this)
                .title(R.string.select_date)
                .customView(R.layout.todo_calendar_layout, wrapInScrollView)
                .positiveText(R.string.positive)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        View calendarView = dialog.getCustomView();//obtengo la vista del activity

                        DatePicker dp = calendarView.findViewById(R.id.datePicker);
                        int yyyy = dp.getYear();
                        int mm = dp.getMonth();
                        int dd = dp.getDayOfMonth();
                        EditText editText = calendarView
                                            .findViewById(R.id.ediTextDescrip);
                        String s = editText.getText().toString();
                        presenter.addTask(s,""+yyyy+"-"+mm+"-"+dd);

                    }
                })
                .show();

        //String descTask = txtDescTask.getText().toString();
        //presenter.addTask(descTask);
    }

    @Override
    public void showTasks(List<TaskViewModel> taskViewModels) {
        taskAdapter.addLstTask(taskViewModels);
        taskAdapter.notifyDataSetChanged();
    }
}
