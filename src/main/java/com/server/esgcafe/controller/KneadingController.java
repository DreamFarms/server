package com.server.esgcafe.controller;

import com.server.esgcafe.domain.dto.kneading.*;
import com.server.esgcafe.exception.*;
import com.server.esgcafe.service.KneadingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/kneading")
@RequiredArgsConstructor
public class KneadingController {

    private final KneadingService kneadingService;

    @PostMapping("/start")
    public Response<KneadingStartResponse> startKneading(@RequestBody KneadingStartRequest request) {
        return Response.success(kneadingService.startKneading(request));
    }

    @PostMapping("/result")
    public Response<KneadingResultResponse> finishKneading(@RequestBody KneadingResultRequest request) {
        return Response.success(kneadingService.finishKneading(request));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Response<ErrorResponse>> handleInvalidBody() {
        ErrorCode error = ErrorCode.INVALID_REQUEST;
        return ResponseEntity.status(error.getStatus())
                .body(Response.error(new ErrorResponse(error.name(), error.getMessage())));
    }
}
