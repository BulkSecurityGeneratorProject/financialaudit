-- ELDE BULUNSUN :)

--- BS ANALIZ VIEW

CREATE OR REPLACE VIEW  BS_ANALIZ_VIEW AS
  SELECT
    SUM(CASE WHEN (hh.kebirkodu = '120') THEN hh.borc ELSE 0 END) AS alacak,
    SUM(CASE WHEN (hh.kebirkodu = '120') THEN 1 ELSE 0 END) AS calacak,
    SUM(CASE WHEN (hh.kebirkodu = '391') THEN hh.alacak ELSE 0 END) AS borc,
    k.vergino AS vergino,
    hh.upload_id AS hh_upload_id,
    k.upload_id AS k_upload_id,
    b.upload_id AS b_upload_id,
    k.id as id
  FROM hesaphareketleri as hh
    INNER JOIN buffer as b ON hh.yevmiyeno = b.yevmiyeno
    INNER JOIN kitap as k ON b.muhasebehesapkodu = k.muhasebekodu
  WHERE  k.muhasebekodu LIKE '120%' AND
         (hh.kebirkodu = '391' OR (hh.muhasebehesapkodu = k.muhasebekodu AND hh.kebirkodu = '120' AND hh.borc > 0))
         AND hh.yevmiyeno > 1
  GROUP BY k.vergino, k.id, hh.upload_id, k.upload_id, b.upload_id;

-- BA ANALIZ
CREATE OR REPLACE VIEW  BA_ANALIZ_VIEW AS
SELECT
  SUM(CASE WHEN (hh.kebirkodu = '320') THEN hh.alacak ELSE 0 END) AS alacak,
  SUM(CASE WHEN (hh.kebirkodu = '320') THEN 1 ELSE 0 END) AS calacak,
  SUM(CASE WHEN (hh.kebirkodu = '191') THEN hh.borc ELSE 0 END) AS borc,
  k.vergino AS vergino,
  hh.upload_id AS hh_upload_id,
  k.upload_id AS k_upload_id,
  b.upload_id AS b_upload_id,
  k.id as id
FROM hesaphareketleri hh
  INNER JOIN buffer b ON hh.yevmiyeno = b.yevmiyeno
  INNER JOIN kitap k ON b.muhasebehesapkodu = k.muhasebekodu
WHERE  k.muhasebekodu LIKE '320%'
       AND (hh.kebirkodu = '191' OR
            (hh.muhasebehesapkodu = k.muhasebekodu AND hh.kebirkodu = '320'AND hh.alacak > 0)
       )
       AND hh.yevmiyeno > 1
GROUP BY k.vergino, k.id, hh.upload_id, k.upload_id, b.upload_id;