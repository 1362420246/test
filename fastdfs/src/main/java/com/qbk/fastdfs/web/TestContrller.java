package com.qbk.fastdfs.web;

import com.qbk.fastdfs.utils.FastDFSClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
public class TestContrller {

    @Autowired
    private FastDFSClientUtil fastDFSClientUtil;

    @PostMapping(value = "/upload/batch", consumes="multipart/form-data")
    public List<String> batchUpload(
            HttpServletRequest request
    ) throws IOException {
        return fastDFSClientUtil.uploadMultipleFiles(request);
    }

    @PostMapping(value = "/upload", consumes="multipart/form-data")
    public String upload(
            MultipartFile file
    ) throws IOException {
        return fastDFSClientUtil.uploadFile(file);
    }
}
