package fr.limsi.rorqual.core.dpe.enums.generalproperties;

/**
 * Created by ricordeau on 23/07/15.
 */
public enum DepartementBatimentEnum {

    AIN("Ain","01",0),
    AISNE("Aisne","02",1),
    ALLIER("Allier","03",2),
    ALPES_DE_HAUTE_PROVENCE("Alpes de Haute Provence","04",3),
    HAUTES_ALPES("Hautes Alpes","05",4),
    ALPES_MARITIMES("Alpes Maritimes","06",5),
    ARDECHE("Ardèche","07",6),
    ARDENNES("Ardennes","08",7),
    ARIEGE("Ariège","09",8),
    AUBE("Aube","10",9),
    AUDE("Aude","11",10),
    AVEYRON("Aveyron","12",11),
    BOUCHES_DU_RHONE("Bouches du Rhône","13",12),
    CALVADOS("Calvados","14",13),
    CANTAL("Cantal","15",14),
    CHARENTE("Charente","16",15),
    CHARENTE_MARITIME("Charente Maritime","17",16),
    CHER("Cher","18",17),
    CORREZE("Corrèze","19",18),
    CORSE_DU_SUD("Corse du Sud","2A",19),
    HAUTE_CORSE("Haute Corse","2B",20),
    COTE_D_OR("Côte d'Or","21",21),
    COTES_D_ARMOR("Côtes d'Armor","22",22),
    CREUSE("Creuse","23",23),
    DORDOGNE("Dordogne","24",24),
    DOUBS("Doubs","25",25),
    DROME("Drome","26",26),
    EURE("Eure","27",27),
    EURE_ET_LOIRE("Eure et Loire","28",28),
    FINISTERE("Finistère","29",29),
    GARD("Gard","30",30),
    HAUTE_GARONNE("Haute Garonne","31",31),
    GERS("Gers","32",32),
    GIRONDE("Gironde","33",33),
    HERAULT("Hérault","34",34),
    ILE_ET_VILAINE("Ile et Vilaine","35",35),
    INDRE("Indre","36",36),
    INDRE_ET_LOIRE("Indre et Loire","37",37),
    ISERE("Isère","38",38),
    JURA("Jura","39",39),
    LANDES("Landes","40",40),
    LOIRE_ET_CHER("Loire et Cher","41",41),
    LOIRE("Loire","42",42),
    HAUTE_LOIRE("Haute Loire","43",43),
    LOIRE_ATLANTIQUE("Loire Atlantique","44",44),
    LOIRET("Loiret","45",45),
    LOT("Lot","46",46),
    LOT_ET_GARONNE("Lot et Garonne","47",47),
    LOZERE("Lozère","48",48),
    MAINE_ET_LOIRE("Maine et Loire","49",49),
    MANCHE("Manche","50",50),
    MARNE("Marne","51",51),
    HAUTE_MARNE("Haute Marne","52",52),
    MAYENNE("Mayenne","53",53),
    MEURTHE_ET_MOSELLE("Meurthe et Moselle","54",54),
    MEUSE("Meuse","55",55),
    MORBIHAN("Meurthe et Moselle","56",56),
    MOSELLE("Moselle","57",57),
    NIEVRE("Nièvre","58",58),
    NORD("Nord","59",59),
    OISE("Oise","60",60),
    ORNE("Orne","61",61),
    PAS_DE_CALAIS("Pas de Calais","62",62),
    PUY_DE_DOME("Puy de Dôme","63",63),
    PYRENEES_ATLANTIQUES("Pyrénées Atlantiques","64",64),
    HAUTES_PYRENEES("Hautes Pyrénées","65",65),
    PYRENNES_ORIENTALES("Pyrénées Orientales","66",66),
    BAS_RHIN("Bas Rhin","67",67),
    HAUT_RHIN("Haut Rhin","68",68),
    RHONE("Rhône","69",69),
    HAUTE_SAONE("Haute Saône","70",70),
    SAONE_ET_LOIRE("Saône et Loire","71",71),
    SARTHE("Sarthe","72",72),
    SAVOIE("Savoie","73",73),
    HAUTE_SAVOIE("Haute Savoie","74",74),
    PARIS("Paris","75",75),
    SEINE_MARITIME("Seine Maritime","76",76),
    SEINE_ET_MARNE("Seine et Marne","77",77),
    YVELINES("Yvelines","78",78),
    DEUX_SEVRES("Deux Sèvres","79",79),
    SOMME("Somme","80",80),
    TARN("Tarn","81",81),
    TARN_ET_GARONNE("Tarn et Garonne","82",82),
    VAR("Var","83",83),
    VAUCLUSE("Vaucluse","84",84),
    VENDEE("Vendée","85",85),
    VIENNE("Vienne","86",86),
    HUATE_VIENNE("Haute Vienne","87",87),
    VOSGES("Vosges","88",88),
    YONNE("Yonne","89",89),
    TERRITOIRE_DE_BELFORT("Territoire de Belfort","90",90),
    ESSONNE("Essonne","91",91),
    HAUTS_DE_SEINE("Hauts de Seine","92",92),
    SEINE_SAINT_DENIS("Seine Saint Denis","93",93),
    VAL_DE_MARNE("Val de Marne","94",94),
    VAL_D_OISE("Val d'Oise","95",95);

    private String dept;
    private String numb;
    private int index;
    DepartementBatimentEnum(String dept,String numb,int index){
        this.dept=dept;
        this.numb=numb;
        this.index = index;
    }

    public String getNameDpt(){
        return (this.numb + " - " + this.dept);
    }

    public int getIndex(){return this.index;}
}
