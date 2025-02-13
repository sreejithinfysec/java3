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
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, request.domainName);
        ResultSet rs = ps.executeQuery();
@Slf4j
@Autowired
@RequestMapping(method=RequestMethod.POST, value="/test-domain", consumes="application/json")
public ResponseEntity<String> testDomain(@RequestBody DomainTestRequest request) {
    log.info("Testing domain {}", request.getDomainName());
    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
         PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE name = ?")) {
        stmt.setString(1, request.getDomainName());
        ResultSet rs = stmt.executeQuery();
        // process result set
    } catch (SQLException ex) {
        log.error("Error testing domain", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    } catch(InvalidDomainException e) {
        log.warn("Invalid domain", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (UnableToTestDomainException e) {
        log.error("Unable to test domain", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<>("Domain tested successfully", HttpStatus.OK);
}

        return new ResponseEntity<>(result, HttpStatus.OK);
    } catch(InvalidDomainException | UnableToTestDomainException | SQLException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

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
