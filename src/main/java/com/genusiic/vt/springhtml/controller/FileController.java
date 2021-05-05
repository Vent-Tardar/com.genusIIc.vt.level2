package com.genusiic.vt.springhtml.controller;

import com.genusiic.vt.springhtml.commons.FileResponse;
import com.genusiic.vt.springhtml.storage.StorageService;
import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FileController {
    private final List<String> listFile = new ArrayList<>();
    private final List<String> list_1 = new ArrayList<>();

    private final StorageService storageService;
    private String org;
    private String mdf;

    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listAllFiles(Model model) {

        model.addAttribute("files", storageService.loadAll().map(
                path -> ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/download/")
                        .path(path.getFileName().toString())
                        .toUriString())
                .collect(Collectors.toList()));

        return "listFiles";
    }

    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {

        Resource resource = storageService.loadAsResource(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/upload-file")
    @ResponseBody
    public FileResponse uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String name = storageService.store(file);

        listFile.add(name);

        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(name)
                .toUriString();

        return new FileResponse(name, uri, file.getContentType(), file.getSize());
    }

    @PostMapping("/upload-multiple-files")
    @ResponseBody
    public List<FileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.stream(files)
                .map(file -> {
                    try {
                        return uploadFile(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }

    @PostMapping("/compare")
    public String compare(Model model) throws IOException {
        List<AbstractDelta<String>> list = new ArrayList<>();
        mdf = listFile.get(listFile.size()-1);
        org = listFile.get(listFile.size()-2);
        try{
            List<String> original = Files.readAllLines(new File(org).toPath());
            List<String> revised = Files.readAllLines(new File(mdf).toPath());

            Patch<String> patch = DiffUtils.diff(original, revised);

            for (AbstractDelta<String> delta : patch.getDeltas()) {
                list.add(delta);
                String str = String.join(" ", list.toString());
                if (str.contains("ChangeDelta")){
                    str = str.replace("[[ChangeDelta, ", "Changes: ").
                            replace("position", "line").
                            replace("lines", "changed").
                            replace("]]", "");
                } else if (str.contains("DeleteDelta")){
                    str = str.replace("[[DeleteDelta, ", "Delete: ").
                            replace("position", "line").
                            replace("lines", "deleted").
                            replace("]]", "");
                } else if (str.contains("InsertDelta")){
                    str = str.replace("[[InsertDelta, ", "Insert: ").
                            replace("position", "line").
                            replace("lines", "inserted").
                            replace("]]", "");
                }
                list.remove(delta);
                list_1.add(str);
            }
        }catch (IOException e){
            System.out.println("Error: " + e.getCause());
        }
        model.addAttribute("compare", list_1);
        return "listFiles";
    }
}