
-- actualizar los creditos de los movimientos de carga inicial de la categoria 12 a 60 creditos
UPDATE (
 SELECT 
   CREDITOS.MOVIMIENTOCREDITOS.CANTIDADCREDITOS AS old_creditos
	FROM
	CREDITOS.MOVIMIENTOCREDITOS
	 INNER JOIN CREDITOS.EMPLEO ON CREDITOS.MOVIMIENTOCREDITOS.EMPLEOID = CREDITOS.EMPLEO.ID
	 INNER JOIN CREDITOS.CATEGORIA ON CREDITOS.EMPLEO.CATEGORIAID = CREDITOS.CATEGORIA.ID
	 WHERE
	 CREDITOS.CATEGORIA.CODIGO = 12 
	 AND CREDITOS.MOVIMIENTOCREDITOS.TIPOMOVIMIENTOCREDITOS='CargaInicialAgenteExistente'
)
SET old_creditos = 60;


-- actualizar la fecha de inicio de empleos a diciembre de 2010
-- BYPASS_UJVC sirve para evitar el error "ORA-01779:cannot modify a column which maps to a non key-preserved table"
-- BYPASS_UJVC no funciona en oracle 11

UPDATE /*+ BYPASS_UJVC */(
SELECT
CREDITOS.EMPLEO.FECHAINICIO as fecha_old
FROM
CREDITOS.EMPLEO,CREDITOS.MOVIMIENTOCREDITOS 
WHERE
CREDITOS.EMPLEO.ID = CREDITOS.MOVIMIENTOCREDITOS.EMPLEOID
AND
CREDITOS.MOVIMIENTOCREDITOS.TIPOMOVIMIENTOCREDITOS = 'CargaInicialAgenteExistente'
)
SET fecha_old = TO_TIMESTAMP('2010-12-01 09:23:35', 'SYYYY-MM-DD HH24:MI:SS:FF7');


-- actualizar los empleos q son CargaInicial con estado ACTIVO
UPDATE /*+ BYPASS_UJVC */(
SELECT
CREDITOS.EMPLEO.ESTADO as estado_old
FROM
CREDITOS.EMPLEO, CREDITOS.MOVIMIENTOCREDITOS
WHERE
CREDITOS.EMPLEO.ID = CREDITOS.MOVIMIENTOCREDITOS.EMPLEOID
AND
(
CREDITOS.MOVIMIENTOCREDITOS.TIPOMOVIMIENTOCREDITOS = 'CargaInicialAgenteExistente'
)
)
SET estado_old = 'ACTIVO';

-- actualizar los empleos q son BajaAgente con estado BAJA
UPDATE /*+ BYPASS_UJVC */(
SELECT
CREDITOS.EMPLEO.ESTADO as estado_old
FROM
CREDITOS.EMPLEO, CREDITOS.MOVIMIENTOCREDITOS
WHERE
CREDITOS.EMPLEO.ID = CREDITOS.MOVIMIENTOCREDITOS.EMPLEOID
AND
(
CREDITOS.MOVIMIENTOCREDITOS.TIPOMOVIMIENTOCREDITOS = 'BajaAgente'
)
)
SET estado_old = 'BAJA';

-- actualizar los empleos q son BajaAgente con estado BAJA
UPDATE /*+ BYPASS_UJVC */(
SELECT
CREDITOS.EMPLEO.ESTADO as estado_old
FROM
CREDITOS.EMPLEO, CREDITOS.MOVIMIENTOCREDITOS
WHERE
CREDITOS.EMPLEO.ID = CREDITOS.MOVIMIENTOCREDITOS.EMPLEOID
AND
(
CREDITOS.MOVIMIENTOCREDITOS.TIPOMOVIMIENTOCREDITOS = 'AscensoAgente'
)
)
SET estado_old = 'PENDIENTE';


-- 
UPDATE (
SELECT CREDITOS.MOVIMIENTOCREDITOS.OBSERVACIONES as old_observaciones
FROM
CREDITOS.AGENTE
INNER JOIN CREDITOS.EMPLEO ON CREDITOS.AGENTE.ID = CREDITOS.EMPLEO.AGENTEID
INNER JOIN CREDITOS.MOVIMIENTOCREDITOS ON CREDITOS.EMPLEO.ID = CREDITOS.MOVIMIENTOCREDITOS.EMPLEOID
INNER JOIN CREDITOS.CATEGORIA ON CREDITOS.EMPLEO.CATEGORIAID = CREDITOS.CATEGORIA.ID
WHERE
CREDITOS.CATEGORIA.CODIGO = '12' and 
MOVIMIENTOCREDITOS.TIPOMOVIMIENTOCREDITOS = 'CargaInicialAgenteExistente'

ORDER BY AGENTE.APELLIDONOMBRE
)
SET old_observaciones = 'Promocion Automatica de categoria 12 a 13';



update CREDITOS.EMPLEO e
       set e.CATEGORIAID = 14
    where exists (
						SELECT e2.id
						FROM 
						CREDITOS.EMPLEO e2
						INNER JOIN CREDITOS.MOVIMIENTOCREDITOS ON e2.ID = CREDITOS.MOVIMIENTOCREDITOS.EMPLEOID
						INNER JOIN CREDITOS.CATEGORIA ON e2.CATEGORIAID = CREDITOS.CATEGORIA.ID
						WHERE
						CREDITOS.CATEGORIA.CODIGO = '12' and 
						MOVIMIENTOCREDITOS.TIPOMOVIMIENTOCREDITOS = 'CargaInicialAgenteExistente' AND
						e2.id = e.id)


-- Update de todos los movimientos de  creditos para setearles el periodo de creditos
-- 07-jul-2013
UPDATE CREDITOS.MOVIMIENTOCREDITOS
	SET CREDITSPERIODID = 2;
	

-- Setear Estado de Movimientos (Solicitado/Otorgado)	
-- 07-jul-2013
UPDATE CREDITOS.MOVIMIENTOCREDITOS
	SET CREDITOS.MOVIMIENTOCREDITOS.GRANTED_STATUS = 'Granted'
	WHERE 	CREDITOS.MOVIMIENTOCREDITOS.TIPOMOVIMIENTOCREDITOS = 'CargaInicialAgenteExistente' OR
			CREDITOS.MOVIMIENTOCREDITOS.TIPOMOVIMIENTOCREDITOS = 'BajaAgente';

UPDATE CREDITOS.MOVIMIENTOCREDITOS
	SET CREDITOS.MOVIMIENTOCREDITOS.GRANTED_STATUS = 'Requested'
	WHERE 	CREDITOS.MOVIMIENTOCREDITOS.TIPOMOVIMIENTOCREDITOS = 'AscensoAgente' OR
			CREDITOS.MOVIMIENTOCREDITOS.TIPOMOVIMIENTOCREDITOS = 'IngresoAgente';

			
