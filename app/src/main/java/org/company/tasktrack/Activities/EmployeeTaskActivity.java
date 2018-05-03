package org.company.tasktrack.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import org.company.tasktrack.Networking.Models.GetAssignedTasksDatum;
import org.company.tasktrack.Networking.Models.TaskCompleteModel;
import org.company.tasktrack.Networking.Models.TaskCompleteResponse;
import org.company.tasktrack.Networking.Models.WriteRemarkModel;
import org.company.tasktrack.Networking.Models.WriteRemarkResponse;
import org.company.tasktrack.Networking.ServiceGenerator;
import org.company.tasktrack.Networking.Services.TaskComplete;
import org.company.tasktrack.Networking.Services.WriteRemarkService;
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
    @BindView(R.id.submitRemark)
    Button submitRemark;
    @BindView(R.id.previousRemarks)
    FloatingActionButton previousRemarks;
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
                            DbHandler.remove(getApplicationContext(),"PendingTasks");
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


    @OnClick(R.id.submitRemark)
    public void submitRemark(){
        if(yourRemark.getText().toString().equals("")){
            Toast.makeText(this,"Please Enter Your Remark",Toast.LENGTH_SHORT).show();
        }else {
            WriteRemarkModel object = new WriteRemarkModel();
            object.setRemark(yourRemark.getText().toString());
            object.setTaskId(data.getId());
            WriteRemarkService remarkService = ServiceGenerator.createService(WriteRemarkService.class, DbHandler.getString(getApplicationContext(), "bearer", ""));
            Call<WriteRemarkResponse> call = remarkService.response(object);
            call.enqueue(new Callback<WriteRemarkResponse>() {
                @Override
                public void onResponse(Call<WriteRemarkResponse> call, Response<WriteRemarkResponse> response) {
                    WriteRemarkResponse remarkResponse=response.body();
                    if(response.code()==200){
                        Toast.makeText(getApplicationContext(),remarkResponse.getMsg(),Toast.LENGTH_LONG).show();
                        if(remarkResponse.isSuccess()){
                            getIntentExtras().putString("type","remark");
                            intentWithFinish(EmployeeActivity.class);
                        }
                    }else if(response.code()==403){
                        DbHandler.unsetSession(getApplicationContext(),"isForcedLoggedOut");
                    }
                }
                @Override
                public void onFailure(Call<WriteRemarkResponse> call, Throwable t) {
                    handleNetworkErrors(t,1);
                }
            });
        }
    }

    @OnClick(R.id.previousRemarks)
    public void setPreviousRemarks(){

        if(data.getHourRemark().size()!=0) {
            String message = "<ul>";
            for (int i = 0; i < data.getHourRemark().size(); i++) {
                message = message + "<li><p>" + data.getHourRemark().get(i).getRemark() + "</p></li><br>";
            }
            message = message + "</ul>";
            Spanned spanned;

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                spanned=Html.fromHtml(message,Html.FROM_HTML_MODE_COMPACT);
            }
            else
            {
                spanned=Html.fromHtml(message);
            }

            new AlertDialog.Builder(this)
                    .setTitle("Your Remarks")
                    .setMessage(spanned)
                    .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create().show();
        }
        else{
            Toast.makeText(getApplicationContext(),"No Previous Remarks",Toast.LENGTH_SHORT).show();
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
