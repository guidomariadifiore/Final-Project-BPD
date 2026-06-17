package it.univaq.disim.bpd.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Service
public class FileService {

    private static final String DIRECTORY_PATH = "requests/";

    public void writeConfirmedRequestToFile(String requestId, Map<String, Object> dataModel) throws IOException {
        Files.createDirectories(Paths.get(DIRECTORY_PATH));

        File file = new File(DIRECTORY_PATH + "order_" + requestId + ".txt");

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setClassForTemplateLoading(this.getClass(), "/templates/");
        cfg.setDefaultEncoding("UTF-8");

        try (FileWriter writer = new FileWriter(file)) {
            Template template = cfg.getTemplate("orderTemplate.ftl");
            template.process(dataModel, writer);
        } catch (TemplateException e) {
            throw new IOException("Failed to process template", e);
        }
    }
}
