package org.company.tasktrack.Activities;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.company.tasktrack.Adapters.Employee.EmployeeTaskImagesAdapter;
import org.company.tasktrack.Networking.Models.GetAssignedTasksDatum;
import org.company.tasktrack.Networking.Models.TaskCompleteModel;
import org.company.tasktrack.Networking.Models.TaskCompleteResponse;
import org.company.tasktrack.Networking.ServiceGenerator;
import org.company.tasktrack.Networking.Services.TaskComplete;
import org.company.tasktrack.R;
import org.company.tasktrack.Utils.DbHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeTaskActivity extends BaseActivity {

    @BindView(R.id.taskTitle)
    TextView taskTitle;
    @BindView(R.id.taskDesc)
    TextView taskDesc;
   @BindView(R.id.remarkManager)
   TextView  remarkManager;
   @BindView(R.id.yourRemark)
    EditText yourRemark;
    @BindView(R.id.taskCompleted)
    Button taskCompleted;
    Gson gson;
    GetAssignedTasksDatum data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_task);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        gson=new Gson();
        Intent intent=getIntent();
        Bundle b=intent.getExtras();
        data=gson.fromJson(b.getString("TaskDetails"),GetAssignedTasksDatum.class);
        taskTitle.setText(data.getName());
        taskDesc.setText(data.getDesc());
        remarkManager.setText(data.getRemarkManager());
    }
    @OnClick(R.id.taskCompleted)
    public void setTaskCompleted(){
        if(yourRemark.getText().toString().equals("")){
            Toast.makeText(this,"Please Enter Your Remark",Toast.LENGTH_SHORT).show();
        }
        else{
            TaskCompleteModel object=new TaskCompleteModel();
            object.setEmpRemark(yourRemark.getText().toString());
            object.setTaskId(data.getId());
            TaskComplete taskComplete= ServiceGenerator.createService(TaskComplete.class, DbHandler.getString(this,"bearer",""));
            Call<TaskCompleteResponse> call=taskComplete.response(object);
            call.enqueue(new Callback<TaskCompleteResponse>() {
                @Override
                public void onResponse(Call<TaskCompleteResponse> call, Response<TaskCompleteResponse> response) {
                    if(response.code()==200){
                        TaskCompleteResponse completeResponse=response.body();
                        if(completeResponse.getSuccess()){
                            Toast.makeText(EmployeeTaskActivity.this, completeResponse.getMsg(), Toast.LENGTH_LONG).show();
                            intentWithFinish(EmployeeActivity.class);
                        }else{
                            Toast.makeText(EmployeeTaskActivity.this,completeResponse.getMsg(),Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        DbHandler.unsetSession(EmployeeTaskActivity.this,"isForcedLoggedOut");
                    }
                }

                @Override
                public void onFailure(Call<TaskCompleteResponse> call, Throwable t) {
                    handleNetworkErrors(t,1);
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }
    @Override
     public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);

    }
}
