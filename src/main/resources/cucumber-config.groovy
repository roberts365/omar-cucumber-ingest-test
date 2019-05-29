targetDeployment = System.getenv("TARGET_DEPLOYMENT")
if (!targetDeployment) {
   targetDeployment = "dev"
}

domainName = System.getenv("DOMAIN_NAME")
if (!domainName) {
   domainName = "ossim.io"
}

s3Bucket = System.getenv("TEST_IMAGE_S3_BUCKET")
if (!s3Bucket) {
   s3Bucket = "o2-test-data/Standard_test_imagery_set"
}

s3BucketUrl = System.getenv("TEST_IMAGE_S3_BUCKET_URL")
if (!s3BucketUrl) {
   s3BucketUrl = "https://s3.amazonaws.com"
}

sqsStagingQueue = System.getenv("SQS_STAGING_QUEUE")
if (!sqsStagingQueue) {
   sqsStagingQueue = "sqs.us-east-1.amazonaws.com"
}

rbtcloudRootDir = System.getenv("RBT_CLOUD_ROOT_DIR")
if (!rbtcloudRootDir) {
   rbtcloudRootDir = "https://omar-${targetDeployment}.${domainName}"
}

switch(targetDeployment) {
   case "dev":
      sqsStagingQueue = "https://${sqsStagingQueue}/320588532383/avro-dev"
      break
   case "stage":
      sqsStagingQueue = "https://${sqsStagingQueue}/320588532383/avro-stage"
      break
   case "prod":
      sqsStagingQueue = "https://${sqsStagingQueue}/320588532383/avro-prod"
      break
   case "blue":
      sqsStagingQueue = "https://${sqsStagingQueue}/320588532383/avro-prod"
      break
   case "green":
      sqsStagingQueue = "https://${sqsStagingQueue}/320588532383/avro-prod"
      break
   case "rel":
      sqsStagingQueue = "https://${sqsStagingQueue}/320588532383/avro-release"
      break
   default:
        sqsStagingQueue = "NOT_ASSIGNED"
      break
}
stagingService = "${rbtcloudRootDir}/omar-stager/dataManager"
wmsServerProperty = "${rbtcloudRootDir}/omar-wms/wms"
wfsServerProperty = "${rbtcloudRootDir}/omar-wfs/wfs"
wfsUrl = "${rbtcloudRootDir}/omar-wfs"
imageSpaceUrl = "${rbtcloudRootDir}/omar-oms/imageSpace"
sqsTimestampName = "Timestamp"

if (System.getProperty("cUname") && System.getProperty("cPword"))
{
    uname = System.getProperty("cUname")
    pword = System.getProperty("cPword")
} else {
    uname = System.getenv("CURL_USER_NAME")
    pword = System.getenv("CURL_PASSWORD")
}
curlUname="${uname}:${pword}"

println("\nOMAR URL being tested: ${rbtcloudRootDir}\n")

waitForStage = 5 // minutes to wait
waitForStageMultiIngest = 30 // minutes to wait for all multi ingest images to stage

// The field names ued in the avro message.
// I set it here because highside and lowside are different
image_id_field_name = "imageId"
observation_date_time_field_name = "observationDateTime"
url_field_name = "uRL"

// Image to be ingested and the information associated with them
image_files = [
    staging_data:[
        images:[
            /*image_01:[
                image_id:"04APR16CS0207001_110646_SM0262R_29N081W_001X___SHH_0101_OBS_IMAG",
                file_name:"04APR16CS0207001_110646_SM0262R_29N081W_001X___SHH_0101_OBS_IMAG.nitf",
                observation_time:"2016-04-04T02:07:00.000Z",
                url:"${s3BucketUrl}/o2-test-data/Standard_test_imagery_set/Cosmo+SkyMED/NITF/04APR16CS0207001_110646_SM0262R_29N081W_001X___SHH_0101_OBS_IMAG.nitf"
            ],*/
            image_02:[
                image_id:"04DEC11050020-M2AS_R1C1-000000185964_01_P001",
                file_name:"04DEC11050020-M2AS_R1C1-000000185964_01_P001.TIF",
                observation_time:"2011-12-04T05:00:20.000Z",
                url:"${s3BucketUrl}/o2-test-data/Standard_test_imagery_set/QuickBird/TIFF/Muliti/04DEC11050020-M2AS_R1C1-000000185964_01_P001.TIF"
            ],
            /*image_03:[
                image_id:"04DEC11050020-P2AS_R1C1-000000185964_01_P001",
                file_name:"04DEC11050020-P2AS_R1C1-000000185964_01_P001.TIF",
                observation_time:"2011-12-04T05:00:20.000Z",
                url:"${s3BucketUrl}/o2-test-data/Standard_test_imagery_set/QuickBird/TIFF/Pan/04DEC11050020-P2AS_R1C1-000000185964_01_P001.TIF"
            ],*/
            /*image_04:[
                image_id:"04MAY16RS0208001_132348_SM0280R_31N111W_001C___SHH_0101_OBS_IMAG",
                file_name:"04MAY16RS0208001_132348_SM0280R_31N111W_001C___SHH_0101_OBS_IMAG.nitf",
                observation_time:"2016-05-04T02:08:00.000Z",
                url:"${s3BucketUrl}/o2-test-data/Standard_test_imagery_set/Radarsat/NITF/04MAY16RS0208001_132348_SM0280R_31N111W_001C___SHH_0101_OBS_IMAG.nitf"
            ],*/
            image_05:[
                image_id:"05FEB09OV05010005V090205M0001912264B220000100072M_001508507",
                file_name:"05FEB09OV05010005V090205M0001912264B220000100072M_001508507.ntf",
                observation_time:"2009-02-05T00:02:01.000Z",
                url:"${s3BucketUrl}/o2-test-data/Standard_test_imagery_set/GeoEye/NITF2_1/multi/05FEB09OV05010005V090205M0001912264B220000100072M_001508507.ntf"
            ],
            image_06:[
                image_id:"05FEB09OV05010005V090205P0001912264B220000100282M_001508507",
                file_name:"05FEB09OV05010005V090205P0001912264B220000100282M_001508507.ntf",
                observation_time:"2009-02-05T00:02:01.000Z",
                url:"${s3BucketUrl}/o2-test-data/Standard_test_imagery_set/GeoEye/NITF2_1/Pan/05FEB09OV05010005V090205P0001912264B220000100282M_001508507.ntf"
            ],
            image_07:[
                image_id:"11MAR08WV010500008MAR11071429-P1BS-005707719010_04_P003",
                file_name:"11MAR08WV010500008MAR11071429-P1BS-005707719010_04_P003.ntf",
                observation_time:"2008-03-11T01:05:00.000Z",
                url:"${s3BucketUrl}/o2-test-data/Standard_test_imagery_set/WorldView/WV2/NITF/11MAR08WV010500008MAR11071429-P1BS-005707719010_04_P003.ntf"
            ],
            /*image_08:[
                image_id:"14AUG20010406-M1BS-053852449040_01_P001",
                file_name:"14AUG20010406-M1BS-053852449040_01_P001.TIF",
                observation_time:"2001-08-14T04:06:00.000Z",
                url:"${s3BucketUrl}/o2-test-data/Standard_test_imagery_set/GeoEye/GeoTIFF/Multi/14AUG20010406-M1BS-053852449040_01_P001.TIF"
            ],*/
            /*image_09:[
                image_id:"14AUG20010406-P1BS-053852449040_01_P001",
                file_name:"14AUG20010406-P1BS-053852449040_01_P001.TIF",
                observation_time:"2001-08-14T04:06:00.000Z",
                url:"${s3BucketUrl}/o2-test-data/Standard_test_imagery_set/GeoEye/GeoTIFF/Pan/14AUG20010406-P1BS-053852449040_01_P001.TIF"
            ],*/
            /*image_10:[
                image_id:"14OCT15165809-M1BS-053951940010_01_P001",
                file_name:"14OCT15165809-M1BS-053951940010_01_P001.TIF",
                observation_time:"2015-10-14T16:58:09.000Z",
                url:"${s3BucketUrl}/o2-test-data/Standard_test_imagery_set/WorldView/WV3/GeoTIFF/Multi/14OCT15165809-M1BS-053951940010_01_P001.TIF"
            ],*/
            /*image_11:[
                image_id:"14OCT15165809-P1BS-053951940010_01_P001",
                file_name:"14OCT15165809-P1BS-053951940010_01_P001.TIF",
                observation_time:"2015-10-14T16:58:09.000Z",
                url:"${s3BucketUrl}/o2-test-data/Standard_test_imagery_set/WorldView/WV3/GeoTIFF/Pan/14OCT15165809-P1BS-053951940010_01_P001.TIF"
            ],*/
            image_12:[
                image_id:"14SEP15TS0107001_100021_SL0023L_25N121E_001X___SVV_0101_OBS_IMAG",
                file_name:"14SEP15TS0107001_100021_SL0023L_25N121E_001X___SVV_0101_OBS_IMAG",
                observation_time:"2015-09-14T01:07:00.000Z",
                url:"${s3BucketUrl}/o2-test-data/Standard_test_imagery_set/TerraSAR-X/NITF2_0/14SEP15TS0107001_100021_SL0023L_25N121E_001X___SVV_0101_OBS_IMAG.ntf"
            ],
            image_13:[
                image_id:"14SEP12113301-M1BS-053951940020_01_P001",
                file_name:"14SEP12113301-M1BS-053951940020_01_P001.TIF",
                observation_time:"2012-09-14T11:33:01.000Z",
                url:"${s3BucketUrl}/o2-test-data/Standard_test_imagery_set/WorldView/WV2/GeoTIFF/Multi/14SEP12113301-M1BS-053951940020_01_P001.TIF"
            ],
            /*image_14:[
                image_id:"16MAY02111606-P1BS-055998375010_01_P013",
                file_name:"16MAY02111606-P1BS-055998375010_01_P013.TIF",
                observation_time:"2016-05-02T00:00:00.000Z",
                url:"${s3BucketUrl}/o2-test-data/Standard_test_imagery_set/Paris/16MAY02111606-P1BS-055998375010_01_P013.TIF"
            ],*/
            image_15:[
                image_id:"16MAY02111607-P1BS-055998375010_01_P014",
                file_name:"16MAY02111607-P1BS-055998375010_01_P014.TIF",
                observation_time:"2016-05-02T00:00:00.000Z",
                url:"${s3BucketUrl}/o2-test-data/Standard_test_imagery_set/Paris/16MAY02111607-P1BS-055998375010_01_P014.TIF"
            ]//,
            /*image_16:[
                image_id:"26JAN16TS0109001_130755_DS0110L_33N106W_001X___SVV_0101_OBS_IMAG",
                file_name:"26JAN16TS0109001_130755_DS0110L_33N106W_001X___SVV_0101_OBS_IMAG.ntf",
                observation_time:"2016-01-26T01:09:00.000Z",
                url:"${s3BucketUrl}/o2-test-data/Standard_test_imagery_set/TerraSAR-X/NITF2_0/26JAN16TS0109001_130755_DS0110L_33N106W_001X___SVV_0101_OBS_IMAG.ntf"
            ],*/
            /*image_17:[
                image_id:"28APR16RS0207001_111844_SL0080R_28N082W_001C___SHH_0101_OBS_IMAG",
                file_name:"28APR16RS0207001_111844_SL0080R_28N082W_001C___SHH_0101_OBS_IMAG",
                observation_time:"2016-04-28T02:07:00.000Z",
                url:"${s3BucketUrl}/o2-test-data/Standard_test_imagery_set/Radarsat/NITF/28APR16RS0207001_111844_SL0080R_28N082W_001C___SHH_0101_OBS_IMAG.nitf"
            ],*/
            /*image_18:[
                image_id:"2010-12-05T221358_RE2_3A-NAC_6683383_113276",
                file_name:"2010-12-05T221358_RE2_3A-NAC_6683383_113276.tif",
                observation_time:"2010-12-05T22:13:58.000Z",
                url:"${s3BucketUrl}/o2-test-data/Standard_test_imagery_set/RapidEye/TIFF/2010-12-05T221358_RE2_3A-NAC_6683383_113276.tif"
            ]*/
        ]
    ]
]