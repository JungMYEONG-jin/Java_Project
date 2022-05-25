package study.datajpa.trace;

public class TraceStatus {

    private TraceId traceId;
    private String message;
    private Long startTimeMs;


    public TraceStatus(TraceId traceId, Long startTimeMs, String message) {
        this.traceId = traceId;
        this.message = message;
        this.startTimeMs = startTimeMs;
    }

    public TraceId getTraceId() {
        return traceId;
    }

    public String getMessage() {
        return message;
    }

    public Long getStartTimeMs() {
        return startTimeMs;
    }
}
