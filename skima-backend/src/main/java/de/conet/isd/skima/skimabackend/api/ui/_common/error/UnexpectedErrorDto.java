package de.conet.isd.skima.skimabackend.api.ui._common.error;

/**
 * Response for Unexpected Errors on Server Side. Those usually require manual investigation and fixes
 * which is part of system maintenance. To support this task, they contain a random Id, which is logged alongside
 * the exception, which makes it easier to find the stacktrace after a user reported the error.
 */
public class UnexpectedErrorDto extends ErrorDto {
    private String errorLogId;

    public UnexpectedErrorDto(String errorLogId) {
        this.errorLogId = errorLogId;
    }

    public String getErrorLogId() {
        return errorLogId;
    }

    public void setErrorLogId(String errorLogId) {
        this.errorLogId = errorLogId;
    }
}
