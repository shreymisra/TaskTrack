package org.company.tasktrack.Networking.Services;

import org.company.tasktrack.Networking.Models.WriteRemarkModel;
import org.company.tasktrack.Networking.Models.WriteRemarkResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by shrey on 19/4/18.
 */

public interface WriteRemarkService {
    @POST("write_remark")
    Call<WriteRemarkResponse> response(@Body WriteRemarkModel object);
}
