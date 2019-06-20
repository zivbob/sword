package entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结果
 *
 * @author ziv
 * @date 2019-01-25
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -3948389268046368059L;

    private Integer state;

    private String msg;

    private T data;

    public Result() {}

    public Result(Integer state, String msg) {
        this.state = state;
        this.msg = msg;
    }

    public Result(Integer state, T data) {
        this.state = state;
        this.data = data;
    }

    public Result(String errorMessage) {
        this.msg = errorMessage;
    }

    public static Result success() {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        return result;
    }

    public static <T>  Result<T> success(T data) {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        result.setData(data);
        return result;
    }

    public static <T>  Result<T> failure(ResultCode resultCode) {
        Result result = new Result();
        result.setResultCode(resultCode);
        return result;
    }

    public static <T>  Result<T> failure(ResultCode resultCode, T data) {
        Result result = new Result();
        result.setResultCode(resultCode);
        result.setData(data);
        return result;
    }

    public static Result createBySuccessCodeMessage(Integer successCode, String successMessage){
        return new Result(successCode, successMessage);
    }

    public static <T>  Result<T> createBySuccess(Integer successCode, T data){
        return new Result(successCode, data);
    }

    public static <T>  Result<T> createBySuccess(String msg, T data) {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static Result createByErrorMessage(String errorMessage){
        Result result = new Result();
        result.setResultCode(ResultCode.ERROR);
        result.setMsg(errorMessage);
        return result;
    }

    public void setResultCode(ResultCode code) {
        this.state = code.code();
        this.msg = code.message();
    }
}
