package io.camunda.example;

import io.camunda.connector.api.annotation.OutboundConnector;
import io.camunda.connector.api.error.ConnectorException;
import io.camunda.connector.api.outbound.OutboundConnectorContext;
import io.camunda.connector.api.outbound.OutboundConnectorFunction;
import io.camunda.connector.generator.java.annotation.ElementTemplate;
import io.camunda.example.dto.MyConnectorRequest;
import io.camunda.example.dto.MyConnectorResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@OutboundConnector(
    name = "MYCONNECTOR",
    inputVariables = { "filePath"},
    type = "io.camunda:template:1")
@ElementTemplate(
    id = "io.camunda.connector.Template.v1",
    name = "Template connector",
    version = 1,
    description = "Describe this connector",
    icon = "icon.svg",
    documentationRef = "https://docs.camunda.io/docs/components/connectors/out-of-the-box-connectors/available-connectors-overview/",
    propertyGroups = {
      
      @ElementTemplate.PropertyGroup(id = "compose", label = "Compose")
    },
    inputDataClass = MyConnectorRequest.class)
public class MyConnectorFunction implements OutboundConnectorFunction {

  private static final Logger LOGGER = LoggerFactory.getLogger(MyConnectorFunction.class);

  @Override
  public Object execute(OutboundConnectorContext context) {
    final var connectorRequest = context.bindVariables(MyConnectorRequest.class);
    return executeConnector(connectorRequest);
  }

  private MyConnectorResult executeConnector(final MyConnectorRequest connectorRequest) {
      // TODO: implement connector logic
      String filePath = connectorRequest.filePath();
      LOGGER.info("File content encoded to Base64");
      try {
          // Read the file content
          byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
          
          // Encode the file content to Base64
          String encodedFileContent = encodeToBase64(fileContent);
          LOGGER.info("File content encoded to Base64"  + encodedFileContent);
         
          return new MyConnectorResult(encodedFileContent);
      } catch (IOException e) {
          LOGGER.error("Error reading the file at " + filePath, e);
          throw new ConnectorException("FILE_READ_ERROR", "Error reading the file at " + filePath, e);
      }
  }
  
  private String encodeToBase64(byte[] fileContent) {
      // Encode the file content to Base64
      return Base64.getEncoder().encodeToString(fileContent);
  }
}