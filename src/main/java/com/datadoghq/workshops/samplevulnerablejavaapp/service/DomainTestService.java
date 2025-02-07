package com.datadoghq.workshops.samplevulnerablejavaapp.service;

import com.datadoghq.workshops.samplevulnerablejavaapp.exception.DomainTestException;
import com.datadoghq.workshops.samplevulnerablejavaapp.exception.InvalidDomainException;
import com.datadoghq.workshops.samplevulnerablejavaapp.exception.UnableToTestDomainException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DomainTestService {

  final static int timeoutMs = 10_000;
  final static Pattern domainValidationRegex = Pattern.compile("^((?!-))(xn--)?[a-z0-9][a-z0-9-_]{0,61}[a-z0-9]{0,1}\\.(xn--)?([a-z0-9\\-]{1,61}|[a-z0-9-]{1,30}\\.[a-z]{2,})", Pattern.CASE_INSENSITIVE);

public String testDomain(String domainName) throws DomainTestException {
    if (!isValidDomainName(domainName)) {
      throw new InvalidDomainException("Invalid domain name: " + domainName + " - don't try to hack us!");
    }

    try {
      CommandLine cmdLine = new CommandLine("ping").addArgument("-c").addArgument("1").addArgument(domainName);
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      DefaultExecutor executor = new DefaultExecutor();
      executor.setStreamHandler(new PumpStreamHandler(outputStream));
      int exitCode = executor.execute(cmdLine);
      if (exitCode != 0) {
        throw new UnableToTestDomainException("Ping returned exit status " + exitCode + ": " + outputStream.toString());
      }
      return outputStream.toString();
    } catch (IOException e) {
      throw new UnableToTestDomainException("Internal error while testing domain: " + e.getMessage());
    } catch (InterruptedException e) {
      throw new UnableToTestDomainException("Timed out pinging domain");
    }
}

  }

  private static boolean isValidDomainName(String domainName) {
    Matcher matcher = domainValidationRegex.matcher(domainName);
    return matcher.find();
  }
}
