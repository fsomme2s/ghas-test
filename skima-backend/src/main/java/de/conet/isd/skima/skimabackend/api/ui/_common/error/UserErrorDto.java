package de.conet.isd.skima.skimabackend.api.ui._common.error;

import de.conet.isd.skima.skimabackend.service._common.error.BusinessException;

import java.util.HashMap;
import java.util.Map;

/**
 * Error that is meant to be displayed to the user; contains an error code . The Client does not have to
 * parse and react to the message (although it could do so if required, of course), but most of the times you
 * just translate the ERROR_CODE into a Text to display.
 */
public class UserErrorDto extends ErrorDto {

    private BusinessException.ERROR_CODE errorCode;
    private Map<String, Object> data = new HashMap<>();

    public UserErrorDto() {
        // required by jackson support
    }

    public UserErrorDto(BusinessException businessException) {
        this.errorCode = businessException.getErrorCode();
        this.data.putAll(businessException.getData());
    }

    public BusinessException.ERROR_CODE getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(BusinessException.ERROR_CODE errorCode) {
        this.errorCode = errorCode;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
