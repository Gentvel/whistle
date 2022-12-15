package com.whistle.starter.exception;

import com.whistle.starter.response.ResponseInterface;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Gentvel
 */
@Getter
@Setter
public class BizException extends RuntimeException{

    private String code;

    public BizException(){
        super(ResponseInterface.FAILURE_MESSAGE);
        this.code = ResponseInterface.FAILURE;
    }
    public BizException(String msg){
        super(msg);
        this.code = ResponseInterface.FAILURE;
    }

    public BizException(String code,String msg){
        super(msg);
        this.code = code;
    }

    public BizException(ResponseInterface resp){
        super(resp.getMsg());
        this.code = resp.getCode();
    }

    @Override
    public String toString() {
        return String.format("BusinessException: code [%s],msg [%s]",code,super.getMessage());
    }
}
