@direct_s3_staging_service
Feature: StagingService

Scenario Outline: [DSTG-01] Make a given IMAGE available for discovery
  Given the image <image-name> is not already indexed - direct s3 ingest
  When the image <image-name> is indexed into OMAR - direct s3 ingest
  Then the image <image-name> should be available - direct s3 ingest

  Examples:
    | image-name |