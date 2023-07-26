package org.secwallet.core.exception;

import org.secwallet.core.model.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BizException extends RuntimeException {
    /**
     * error code
     */
    protected final ResultCode resultCode;

    /**
     *
     * This is to harmonize some unnecessary places, redundant fields
     */
    private String code;

    /**
     * No-argument default constructor UNSPECIFIED
     */
    public BizException() {
        super(ResultCode.UNSPECIFIED.getMsg());
        this.resultCode = ResultCode.UNSPECIFIED;
    }

    /**
     * Specify the error code to construct a generic exception
     *
     * @param resultCode
     */
    public BizException(final ResultCode resultCode) {
        super(resultCode.getMsg());
        this.resultCode = resultCode;
    }

    /**
     * 指定详细描述构造通用异常
     *
     * @param detailedMessage 详细描述
     */
    public BizException(final String detailedMessage) {
        super(detailedMessage);
        this.resultCode = ResultCode.UNSPECIFIED;
    }

    /**
     * 指定Throwable构造通用异常
     *
     * @param t 导火索
     */
    public BizException(final Throwable t) {
        super(t);
        this.resultCode = ResultCode.UNSPECIFIED;
    }

    /**
     * 构造通用异常
     *
     * @param resultCode      错误码
     * @param detailedMessage 详细描述
     */
    public BizException(final ResultCode resultCode, final String detailedMessage) {
        super(detailedMessage);
        this.resultCode = resultCode;
    }

    /**
     * 构造通用异常
     *
     * @param resultCode 错误码
     * @param t          导火索
     */
    public BizException(final ResultCode resultCode, final Throwable t) {
        super(resultCode.getMsg(), t);
        this.resultCode = resultCode;
    }

    /**
     * 构造通用异常
     *
     * @param detailedMessage 详细描述
     * @param t               导火索
     */
    public BizException(final String detailedMessage, final Throwable t) {
        super(detailedMessage, t);
        this.resultCode = ResultCode.UNSPECIFIED;
    }

    /**
     * 构造通用异常
     *
     * @param resultCode      错误码
     * @param detailedMessage 详细描述
     * @param t               导火索
     */
    public BizException(final ResultCode resultCode, final String detailedMessage,
                        final Throwable t) {
        super(detailedMessage, t);
        this.resultCode = resultCode;
    }

    /**
     * Getter method for property <tt>resultCode</tt>.
     *
     * @return property value of resultCode
     */
    public ResultCode getResultCode() {
        return resultCode;
    }
}
