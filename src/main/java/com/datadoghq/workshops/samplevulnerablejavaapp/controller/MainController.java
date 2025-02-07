package com.datadoghq.workshops.samplevulnerablejavaapp.controller;

import com.datadoghq.workshops.samplevulnerablejavaapp.exception.FileForbiddenFileException;
import com.datadoghq.workshops.samplevulnerablejavaapp.exception.FileReadException;
import com.datadoghq.workshops.samplevulnerablejavaapp.exception.InvalidDomainException;
import com.datadoghq.workshops.samplevulnerablejavaapp.exception.UnableToTestDomainException;
import com.datadoghq.workshops.samplevulnerablejavaapp.http.DomainTestRequest;
import com.datadoghq.workshops.samplevulnerablejavaapp.http.ViewFileRequest;
import com.datadoghq.workshops.samplevulnerablejavaapp.http.WebsiteTestRequest;
import com.datadoghq.workshops.samplevulnerablejavaapp.service.DomainTestService;
import com.datadoghq.workshops.samplevulnerablejavaapp.service.FileService;
import com.datadoghq.workshops.samplevulnerablejavaapp.service.WebsiteTestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MainController {

  public Logger log = LoggerFactory.getLogger(MainController.class);

  @Autowired
  private DomainTestService domainTestService;

  @Autowired
  private WebsiteTestService websiteTestService;

  @Autowired
  private FileService fileService;

@RequestMapping(method=RequestMethod.POST, value="/test-domain", consumes="application/json")
public ResponseEntity<String> testDomain(@RequestBody DomainTestRequest request) {
    log.info("Testing domain " + request.domainName);
    try {
        String sql = "SELECT * FROM domains WHERE name = ?";
        try (PreparedStatement ps = dataSource.getConnection().prepareStatement(sql)) {
            ps.setString(1, request.domainName);
            try (ResultSet rs = ps.executeQuery()) {
                // Process the result set
            }
        }
    } catch(SQLException e) {
        // Handle SQL exceptions
    }
}

  }

  @RequestMapping(method=RequestMethod.POST, value="/test-website", consumes="application/json")
  public ResponseEntity<String> testWebsite(@RequestBody WebsiteTestRequest request) {
    log.info("Testing website " + request.url);
    String result = websiteTestService.testWebsite(request);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

@RequestMapping(method=RequestMethod.POST, value="/view-file", consumes="application/json")
public ResponseEntity<String> viewFile(@RequestBody ViewFileRequest request) {
    // Use SLF4J's sanitizing logger to prevent log forging
    Logger log = LoggerFactory.getLogger(this.getClass());
    log.info("Reading file {}", request.path);
    try {
        String result = fileService.readFile(request.path);
        return new ResponseEntity<>(result, HttpStatus.OK);
    } catch (FileForbiddenFileException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    } catch (FileReadException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}

  }

  }

}
