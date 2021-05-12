package com.genusiic.vt.springhtml.controller;

import com.genusiic.vt.springhtml.commons.FileResponse;
import com.genusiic.vt.springhtml.storage.StorageService;
import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class FileController {
    private static final Logger logger = LogManager.getLogger(FileController.class);
    private final List<String> list_1 = new ArrayList<>();

    private final StorageService storageService;

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

    @GetMapping("/compare")
    public String compare(Model model) {
        List<AbstractDelta<String>> list = new ArrayList<>();
        list_1.clear();
        try{
            StringBuilder org = new StringBuilder("C:\\Users\\Genus\\Documents\\uploadFiles\\");
            StringBuilder mdf = new StringBuilder("C:\\Users\\Genus\\Documents\\uploadFiles\\");
            File folder = new File("C:/Users/Genus/Documents/uploadFiles/");
            String[] files = folder.list();
            assert files != null;
            if (files.length < 2){
                list_1.add("Not enough files to compare");
            } else if (files.length > 2){
                list_1.add("More than two files uploaded");
                for (File file : Objects.requireNonNull(new File(String.valueOf(folder)).listFiles()))
                    if (file.isFile()) file.delete();
            } else {
                for (File file : Objects.requireNonNull(folder.listFiles())) {
                    if (file.getName().contains("1")) {
                        org.append(file.getName());
                    }
                    if (file.getName().contains("2")) {
                        mdf.append(file.getName());
                    }
                }
                logger.info("File comparison started.");
                logger.info("Files are being compared.");

                List<String> original = Files.readAllLines(new File(org.toString()).toPath());
                List<String> revised = Files.readAllLines(new File(mdf.toString()).toPath());

                Patch<String> patch = DiffUtils.diff(original, revised);

                for (AbstractDelta<String> delta : patch.getDeltas()) {
                    list.add(delta);
                    String str = String.join(" ", list.toString());
                    if (str.contains("ChangeDelta")) {
                        str = str.replace("[[ChangeDelta, ", "Changes: ").
                                replace("position", "line").
                                replace("lines", "changed").
                                replace("]]", "");
                    } else if (str.contains("DeleteDelta")) {
                        str = str.replace("[[DeleteDelta, ", "Delete: ").
                                replace("position", "line").
                                replace("lines", "deleted").
                                replace("]]", "");
                    } else if (str.contains("InsertDelta")) {
                        str = str.replace("[[InsertDelta, ", "Insert: ").
                                replace("position", "line").
                                replace("lines", "inserted").
                                replace("]]", "");
                    }
                    list.remove(delta);
                    list_1.add(str);
                }
                for (File file : Objects.requireNonNull(new File(String.valueOf(folder)).listFiles()))
                    if (file.isFile()) file.delete();
            }
            logger.info("File comparison ended.");
        }catch (IOException e){
            System.out.println("Error: " + e.getCause());
        }
        model.addAttribute("compare", list_1);
        return "compareFiles";
    }
}