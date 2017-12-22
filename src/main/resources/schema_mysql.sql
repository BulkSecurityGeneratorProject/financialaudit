drop table if exists oauth_client_details;
create table oauth_client_details ( client_id VARCHAR(256) PRIMARY KEY, resource_ids VARCHAR(256), client_secret VARCHAR(256), scope VARCHAR(256),authorized_grant_types VARCHAR(256),web_server_redirect_uri VARCHAR(256),authorities VARCHAR(256),access_token_validity INTEGER,refresh_token_validity INTEGER,additional_information VARCHAR(4096),autoapprove VARCHAR(256));
drop table if exists oauth_client_token;
create table oauth_client_token (token_id VARCHAR(256),token BLOB,authentication_id VARCHAR(256),user_name VARCHAR(256),client_id VARCHAR(256));
drop table if exists oauth_access_token;
create table oauth_access_token (token_id VARCHAR(256),token BLOB,authentication_id VARCHAR(256),user_name VARCHAR(256),client_id VARCHAR(256),authentication BLOB,refresh_token VARCHAR(256));
drop table if exists oauth_refresh_token;
create table oauth_refresh_token (token_id VARCHAR(256),token BLOB,authentication BLOB);
drop table if exists oauth_code;
create table oauth_code (code VARCHAR(256), authentication BLOB);
drop table if exists oauth_approvals;
create table oauth_approvals (userId VARCHAR(256),clientId VARCHAR(256),scope VARCHAR(256),status VARCHAR(10),expiresAt TIMESTAMP,lastModifiedAt TIMESTAMP);

#--- BA Analiz icin view
DROP TABLE IF EXISTS BS_ANALIZ_VIEW;
CREATE OR REPLACE VIEW  BS_ANALIZ_VIEW AS SELECT SUM(CASE WHEN (hh.kebirkodu = '120') THEN hh.borc ELSE 0 END) AS alacak, SUM(CASE WHEN (hh.kebirkodu = '120') THEN 1 ELSE 0 END) AS calacak, SUM(CASE WHEN (hh.kebirkodu = '391') THEN hh.alacak ELSE 0 END) AS borc, k.vergino,hh.upload_id AS hh_upload_id,k.upload_id AS k_upload_id, b.upload_id AS b_upload_id, k.id as id FROM hesaphareketleri as hh INNER JOIN buffer as b ON hh.yevmiyeno = b.yevmiyeno INNER JOIN kitap as k ON b.muhasebehesapkodu = k.muhasebekodu WHERE  k.muhasebekodu LIKE '120%' AND (hh.kebirkodu = '391' OR (hh.muhasebehesapkodu = k.muhasebekodu AND hh.kebirkodu = '120' AND hh.borc > 0)) AND hh.yevmiyeno > 1 GROUP BY k.vergino, k.id, hh.upload_id, k.upload_id, b.upload_id;
#--- BA Analiz icin view
DROP TABLE IF EXISTS BA_ANALIZ_VIEW;
CREATE OR REPLACE VIEW  BA_ANALIZ_VIEW AS SELECT SUM(CASE WHEN (hh.kebirkodu = '320') THEN hh.alacak ELSE 0 END) AS alacak, SUM(CASE WHEN (hh.kebirkodu = '320') THEN 1 ELSE 0 END) AS calacak, SUM(CASE WHEN (hh.kebirkodu = '191') THEN hh.borc ELSE 0 END) AS borc, k.vergino AS vergino, hh.upload_id AS hh_upload_id,k.upload_id AS k_upload_id, b.upload_id AS b_upload_id, k.id as id FROM hesaphareketleri hh INNER JOIN buffer b ON hh.yevmiyeno = b.yevmiyeno INNER JOIN kitap k ON b.muhasebehesapkodu = k.muhasebekodu WHERE  k.muhasebekodu LIKE '320%' AND (hh.kebirkodu = '191' OR (hh.muhasebehesapkodu = k.muhasebekodu AND hh.kebirkodu = '320'AND hh.alacak > 0)) AND hh.yevmiyeno > 1 GROUP BY k.vergino, k.id, hh.upload_id, k.upload_id, b.upload_id;