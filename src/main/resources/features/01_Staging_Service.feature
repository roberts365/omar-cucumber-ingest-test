@staging_service
Feature: StagingService

Scenario Outline: [STG-01] Make a given IMAGE available for discovery
  Given the image <image-name> is not already staged
  When the image <image-name> avro message is placed on the SQS
  Then the image <image-name> should be discoverable
  And it should have a thumbnail
  And a WMS call should produce an image

  Examples:
    | image-name |
    | 2010-12-05T221358_RE2_3A-NAC_6683383_113276 |
    | 28APR16RS0207001_111844_SL0080R_28N082W_001C___SHH_0101_OBS_IMAG |
    | 26JAN16TS0109001_130755_DS0110L_33N106W_001X___SVV_0101_OBS_IMAG |
    | 16MAY02111607-P1BS-055998375010_01_P014 |
    | 16MAY02111606-P1BS-055998375010_01_P013 |
    | 14SEP12113301-M1BS-053951940020_01_P001 |
    | 14SEP15TS0107001_100021_SL0023L_25N121E_001X___SVV_0101_OBS_IMAG |
    | 14OCT15165809-P1BS-053951940010_01_P001 |
    | 14OCT15165809-M1BS-053951940010_01_P001 |
    | 14AUG20010406-P1BS-053852449040_01_P001 |
    | 14AUG20010406-M1BS-053852449040_01_P001 |
    | 11MAR08WV010500008MAR11071429-P1BS-005707719010_04_P003 |
    | 05FEB09OV05010005V090205P0001912264B220000100282M_001508507 |
    | 05FEB09OV05010005V090205M0001912264B220000100072M_001508507 |
    | 04MAY16RS0208001_132348_SM0280R_31N111W_001C___SHH_0101_OBS_IMAG |
    | 04DEC11050020-P2AS_R1C1-000000185964_01_P001 |
    | 04DEC11050020-M2AS_R1C1-000000185964_01_P001 |
    | 04APR16CS0207001_110646_SM0262R_29N081W_001X___SHH_0101_OBS_IMAG |