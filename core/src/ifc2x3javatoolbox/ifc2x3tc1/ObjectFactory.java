package ifc2x3javatoolbox.ifc2x3tc1;
/**
 * Copyright: CC BY-NC-SA 3.0 DE (cc) 2013 Eike Tauscher and Michael Theiler<br><br>
 * The whole package including this class is licensed under<br>
 * <a rel='license' href='http://creativecommons.org/licenses/by-nc-sa/3.0/de/deed.en/'>
 * Creative Commons Attribution-Non-Commercial-Share Alike 3.0 Germany</a>.<br><br>
 * If you are using the package or parts of it in any commercial way, a commercial license is required. 
 * Visit <a href='http://www.ifctoolsproject.com'>http://www.ifctoolsproject.com</a> for more information
 * or contact us directly: <a href='mailto:info@ifctoolsproject.com'>info@ifctoolsproject.com</a><br>
 */
public class ObjectFactory extends java.util.HashMap<String, ObjectFactory.ClassBuilder>
{	private static final long serialVersionUID = 1L;
	public static java.util.HashMap<String, ClassBuilder> FACTORY_MAP = new ObjectFactory();

	interface ClassBuilder
	{
		Object instance();
	}

	public static Object createInstance(String entityName)
	{
		return FACTORY_MAP.get(entityName).instance();
	}

	public ObjectFactory()
	{
		this.put("IFCSEQUENCEENUM", new IfcSequenceEnumBuilder());
		this.put("IFCWORKCONTROLTYPEENUM", new IfcWorkControlTypeEnumBuilder());
		this.put("IFCINTEGERCOUNTRATEMEASURE", new IfcIntegerCountRateMeasureBuilder());
		this.put("IFCPROFILETYPEENUM", new IfcProfileTypeEnumBuilder());
		this.put("IFCMODULUSOFSUBGRADEREACTIONMEASURE", new IfcModulusOfSubgradeReactionMeasureBuilder());
		this.put("IFCRATIOMEASURE", new IfcRatioMeasureBuilder());
		this.put("IFCARITHMETICOPERATORENUM", new IfcArithmeticOperatorEnumBuilder());
		this.put("IFCSERVICELIFEFACTORTYPEENUM", new IfcServiceLifeFactorTypeEnumBuilder());
		this.put("IFCTRIMMINGPREFERENCE", new IfcTrimmingPreferenceBuilder());
		this.put("IFCENVIRONMENTALIMPACTCATEGORYENUM", new IfcEnvironmentalImpactCategoryEnumBuilder());
		this.put("IFCPROPERTYSOURCEENUM", new IfcPropertySourceEnumBuilder());
		this.put("IFCDOCUMENTSTATUSENUM", new IfcDocumentStatusEnumBuilder());
		this.put("IFCWINDOWSTYLECONSTRUCTIONENUM", new IfcWindowStyleConstructionEnumBuilder());
		this.put("IFCSTATEENUM", new IfcStateEnumBuilder());
		this.put("IFCPLATETYPEENUM", new IfcPlateTypeEnumBuilder());
		this.put("IFCCONDENSERTYPEENUM", new IfcCondenserTypeEnumBuilder());
		this.put("IFCPHYSICALORVIRTUALENUM", new IfcPhysicalOrVirtualEnumBuilder());
		this.put("IFCELECTRICCHARGEMEASURE", new IfcElectricChargeMeasureBuilder());
		this.put("IFCMASSFLOWRATEMEASURE", new IfcMassFlowRateMeasureBuilder());
		this.put("IFCPOSITIVELENGTHMEASURE", new IfcPositiveLengthMeasureBuilder());
		this.put("IFCVOLUMETRICFLOWRATEMEASURE", new IfcVolumetricFlowRateMeasureBuilder());
		this.put("IFCCOMPOUNDPLANEANGLEMEASURE", new IfcCompoundPlaneAngleMeasureBuilder());
		this.put("IFCYEARNUMBER", new IfcYearNumberBuilder());
		this.put("IFCELEMENTCOMPOSITIONENUM", new IfcElementCompositionEnumBuilder());
		this.put("IFCJUNCTIONBOXTYPEENUM", new IfcJunctionBoxTypeEnumBuilder());
		this.put("IFCCOOLINGTOWERTYPEENUM", new IfcCoolingTowerTypeEnumBuilder());
		this.put("IFCTIMESTAMP", new IfcTimeStampBuilder());
		this.put("IFCCOMPLEXNUMBER", new IfcComplexNumberBuilder());
		this.put("IFCSANITARYTERMINALTYPEENUM", new IfcSanitaryTerminalTypeEnumBuilder());
		this.put("IFCTHERMALRESISTANCEMEASURE", new IfcThermalResistanceMeasureBuilder());
		this.put("IFCTUBEBUNDLETYPEENUM", new IfcTubeBundleTypeEnumBuilder());
		this.put("IFCNORMALISEDRATIOMEASURE", new IfcNormalisedRatioMeasureBuilder());
		this.put("IFCACTIONSOURCETYPEENUM", new IfcActionSourceTypeEnumBuilder());
		this.put("IFCDAYLIGHTSAVINGHOUR", new IfcDaylightSavingHourBuilder());
		this.put("SCHEMANAME", new SchemaNameBuilder());
		this.put("IFCLUMINOUSINTENSITYMEASURE", new IfcLuminousIntensityMeasureBuilder());
		this.put("IFCELECTRICTIMECONTROLTYPEENUM", new IfcElectricTimeControlTypeEnumBuilder());
		this.put("IFCPUMPTYPEENUM", new IfcPumpTypeEnumBuilder());
		this.put("IFCROTATIONALSTIFFNESSMEASURE", new IfcRotationalStiffnessMeasureBuilder());
		this.put("IFCLINEARFORCEMEASURE", new IfcLinearForceMeasureBuilder());
		this.put("IFCPROJECTORDERTYPEENUM", new IfcProjectOrderTypeEnumBuilder());
		this.put("IFCTIMEMEASURE", new IfcTimeMeasureBuilder());
		this.put("IFCTEXTFONTNAME", new IfcTextFontNameBuilder());
		this.put("IFCDIMENSIONEXTENTUSAGE", new IfcDimensionExtentUsageBuilder());
		this.put("TIMESTAMPTEXT", new TimeStampTextBuilder());
		this.put("IFCOBJECTIVEENUM", new IfcObjectiveEnumBuilder());
		this.put("IFCSPECULARROUGHNESS", new IfcSpecularRoughnessBuilder());
		this.put("IFCELECTRICCAPACITANCEMEASURE", new IfcElectricCapacitanceMeasureBuilder());
		this.put("IFCRADIOACTIVITYMEASURE", new IfcRadioActivityMeasureBuilder());
		this.put("IFCMAGNETICFLUXMEASURE", new IfcMagneticFluxMeasureBuilder());
		this.put("IFCMEMBERTYPEENUM", new IfcMemberTypeEnumBuilder());
		this.put("IFCSOLIDANGLEMEASURE", new IfcSolidAngleMeasureBuilder());
		this.put("IFCSIPREFIX", new IfcSIPrefixBuilder());
		this.put("IFCGLOBALORLOCALENUM", new IfcGlobalOrLocalEnumBuilder());
		this.put("IFCACTUATORTYPEENUM", new IfcActuatorTypeEnumBuilder());
		this.put("IFCROTATIONALFREQUENCYMEASURE", new IfcRotationalFrequencyMeasureBuilder());
		this.put("IFCELEMENTASSEMBLYTYPEENUM", new IfcElementAssemblyTypeEnumBuilder());
		this.put("IFCDAYINMONTHNUMBER", new IfcDayInMonthNumberBuilder());
		this.put("IFCCURRENCYENUM", new IfcCurrencyEnumBuilder());
		this.put("IFCPRESSUREMEASURE", new IfcPressureMeasureBuilder());
		this.put("IFCENERGYSEQUENCEENUM", new IfcEnergySequenceEnumBuilder());
		this.put("IFCGASTERMINALTYPEENUM", new IfcGasTerminalTypeEnumBuilder());
		this.put("IFCWALLTYPEENUM", new IfcWallTypeEnumBuilder());
		this.put("IFCBSPLINECURVEFORM", new IfcBSplineCurveFormBuilder());
		this.put("IFCSERVICELIFETYPEENUM", new IfcServiceLifeTypeEnumBuilder());
		this.put("IFCMINUTEINHOUR", new IfcMinuteInHourBuilder());
		this.put("IFCPROJECTORDERRECORDTYPEENUM", new IfcProjectOrderRecordTypeEnumBuilder());
		this.put("IFCTEXT", new IfcTextBuilder());
		this.put("IFCSURFACESIDE", new IfcSurfaceSideBuilder());
		this.put("IFCCONTROLLERTYPEENUM", new IfcControllerTypeEnumBuilder());
		this.put("IFCFORCEMEASURE", new IfcForceMeasureBuilder());
		this.put("IFCFLOWMETERTYPEENUM", new IfcFlowMeterTypeEnumBuilder());
		this.put("IFCSOUNDPRESSUREMEASURE", new IfcSoundPressureMeasureBuilder());
		this.put("IFCFONTVARIANT", new IfcFontVariantBuilder());
		this.put("IFCACCELERATIONMEASURE", new IfcAccelerationMeasureBuilder());
		this.put("IFCELECTRICDISTRIBUTIONPOINTFUNCTIONENUM", new IfcElectricDistributionPointFunctionEnumBuilder());
		this.put("IFCFREQUENCYMEASURE", new IfcFrequencyMeasureBuilder());
		this.put("IFCBENCHMARKENUM", new IfcBenchmarkEnumBuilder());
		this.put("IFCCHANGEACTIONENUM", new IfcChangeActionEnumBuilder());
		this.put("IFCCABLESEGMENTTYPEENUM", new IfcCableSegmentTypeEnumBuilder());
		this.put("IFCMASSDENSITYMEASURE", new IfcMassDensityMeasureBuilder());
		this.put("IFCLUMINOUSFLUXMEASURE", new IfcLuminousFluxMeasureBuilder());
		this.put("IFCLABEL", new IfcLabelBuilder());
		this.put("IFCDUCTSILENCERTYPEENUM", new IfcDuctSilencerTypeEnumBuilder());
		this.put("IFCDOORSTYLECONSTRUCTIONENUM", new IfcDoorStyleConstructionEnumBuilder());
		this.put("IFCLAMPTYPEENUM", new IfcLampTypeEnumBuilder());
		this.put("IFCFONTWEIGHT", new IfcFontWeightBuilder());
		this.put("IFCROLEENUM", new IfcRoleEnumBuilder());
		this.put("IFCREFLECTANCEMETHODENUM", new IfcReflectanceMethodEnumBuilder());
		this.put("IFCWINDOWPANELOPERATIONENUM", new IfcWindowPanelOperationEnumBuilder());
		this.put("IFCSURFACETEXTUREENUM", new IfcSurfaceTextureEnumBuilder());
		this.put("IFCELECTRICCURRENTMEASURE", new IfcElectricCurrentMeasureBuilder());
		this.put("IFCASSEMBLYPLACEENUM", new IfcAssemblyPlaceEnumBuilder());
		this.put("IFCEVAPORATIVECOOLERTYPEENUM", new IfcEvaporativeCoolerTypeEnumBuilder());
		this.put("IFCSPECIFICHEATCAPACITYMEASURE", new IfcSpecificHeatCapacityMeasureBuilder());
		this.put("IFCDATAORIGINENUM", new IfcDataOriginEnumBuilder());
		this.put("IFCFILTERTYPEENUM", new IfcFilterTypeEnumBuilder());
		this.put("IFCPOSITIVERATIOMEASURE", new IfcPositiveRatioMeasureBuilder());
		this.put("IFCTEXTTRANSFORMATION", new IfcTextTransformationBuilder());
		this.put("IFCDIRECTIONSENSEENUM", new IfcDirectionSenseEnumBuilder());
		this.put("IFCTEXTPATH", new IfcTextPathBuilder());
		this.put("IFCMONETARYMEASURE", new IfcMonetaryMeasureBuilder());
		this.put("IFCELECTRICFLOWSTORAGEDEVICETYPEENUM", new IfcElectricFlowStorageDeviceTypeEnumBuilder());
		this.put("IFCTORQUEMEASURE", new IfcTorqueMeasureBuilder());
		this.put("IFCWINDOWSTYLEOPERATIONENUM", new IfcWindowStyleOperationEnumBuilder());
		this.put("IFCILLUMINANCEMEASURE", new IfcIlluminanceMeasureBuilder());
		this.put("IFCTRANSFORMERTYPEENUM", new IfcTransformerTypeEnumBuilder());
		this.put("IFCANALYSISTHEORYTYPEENUM", new IfcAnalysisTheoryTypeEnumBuilder());
		this.put("IFCFLOWINSTRUMENTTYPEENUM", new IfcFlowInstrumentTypeEnumBuilder());
		this.put("IFCHUMIDIFIERTYPEENUM", new IfcHumidifierTypeEnumBuilder());
		this.put("IFCMAGNETICFLUXDENSITYMEASURE", new IfcMagneticFluxDensityMeasureBuilder());
		this.put("IFCCURVATUREMEASURE", new IfcCurvatureMeasureBuilder());
		this.put("IFCKINEMATICVISCOSITYMEASURE", new IfcKinematicViscosityMeasureBuilder());
		this.put("IFCPHMEASURE", new IfcPHMeasureBuilder());
		this.put("IFCHEATFLUXDENSITYMEASURE", new IfcHeatFluxDensityMeasureBuilder());
		this.put("IFCCONNECTIONTYPEENUM", new IfcConnectionTypeEnumBuilder());
		this.put("IFCRAMPFLIGHTTYPEENUM", new IfcRampFlightTypeEnumBuilder());
		this.put("IFCSPECULAREXPONENT", new IfcSpecularExponentBuilder());
		this.put("IFCMASSMEASURE", new IfcMassMeasureBuilder());
		this.put("IFCTHERMALLOADSOURCEENUM", new IfcThermalLoadSourceEnumBuilder());
		this.put("IFCEVAPORATORTYPEENUM", new IfcEvaporatorTypeEnumBuilder());
		this.put("IFCSECTIONTYPEENUM", new IfcSectionTypeEnumBuilder());
		this.put("IFCDOORPANELPOSITIONENUM", new IfcDoorPanelPositionEnumBuilder());
		this.put("IFCSIUNITNAME", new IfcSIUnitNameBuilder());
		this.put("IFCWASTETERMINALTYPEENUM", new IfcWasteTerminalTypeEnumBuilder());
		this.put("IFCUNITENUM", new IfcUnitEnumBuilder());
		this.put("IFCTIMESERIESSCHEDULETYPEENUM", new IfcTimeSeriesScheduleTypeEnumBuilder());
		this.put("IFCLIGHTEMISSIONSOURCEENUM", new IfcLightEmissionSourceEnumBuilder());
		this.put("IFCELECTRICRESISTANCEMEASURE", new IfcElectricResistanceMeasureBuilder());
		this.put("IFCTHERMALCONDUCTIVITYMEASURE", new IfcThermalConductivityMeasureBuilder());
		this.put("IFCELECTRICMOTORTYPEENUM", new IfcElectricMotorTypeEnumBuilder());
		this.put("IFCLIGHTFIXTURETYPEENUM", new IfcLightFixtureTypeEnumBuilder());
		this.put("IFCBUILDINGELEMENTPROXYTYPEENUM", new IfcBuildingElementProxyTypeEnumBuilder());
		this.put("IFCPILETYPEENUM", new IfcPileTypeEnumBuilder());
		this.put("IFCTHERMODYNAMICTEMPERATUREMEASURE", new IfcThermodynamicTemperatureMeasureBuilder());
		this.put("IFCDOORPANELOPERATIONENUM", new IfcDoorPanelOperationEnumBuilder());
		this.put("IFCFONTSTYLE", new IfcFontStyleBuilder());
		this.put("IFCLENGTHMEASURE", new IfcLengthMeasureBuilder());
		this.put("IFCMOMENTOFINERTIAMEASURE", new IfcMomentOfInertiaMeasureBuilder());
		this.put("IFCREAL", new IfcRealBuilder());
		this.put("IFCCURTAINWALLTYPEENUM", new IfcCurtainWallTypeEnumBuilder());
		this.put("IFCWARPINGMOMENTMEASURE", new IfcWarpingMomentMeasureBuilder());
		this.put("IFCCOUNTMEASURE", new IfcCountMeasureBuilder());
		this.put("IFCIONCONCENTRATIONMEASURE", new IfcIonConcentrationMeasureBuilder());
		this.put("IFCPLANEANGLEMEASURE", new IfcPlaneAngleMeasureBuilder());
		this.put("IFCELECTRICCONDUCTANCEMEASURE", new IfcElectricConductanceMeasureBuilder());
		this.put("IFCFIRESUPPRESSIONTERMINALTYPEENUM", new IfcFireSuppressionTerminalTypeEnumBuilder());
		this.put("IFCMONTHINYEARNUMBER", new IfcMonthInYearNumberBuilder());
		this.put("IFCDYNAMICVISCOSITYMEASURE", new IfcDynamicViscosityMeasureBuilder());
		this.put("IFCSECTIONMODULUSMEASURE", new IfcSectionModulusMeasureBuilder());
		this.put("IFCHOURINDAY", new IfcHourInDayBuilder());
		this.put("IFCROTATIONALMASSMEASURE", new IfcRotationalMassMeasureBuilder());
		this.put("IFCBOOLEANOPERATOR", new IfcBooleanOperatorBuilder());
		this.put("IFCELECTRICHEATERTYPEENUM", new IfcElectricHeaterTypeEnumBuilder());
		this.put("IFCOBJECTTYPEENUM", new IfcObjectTypeEnumBuilder());
		this.put("IFCINTERNALOREXTERNALENUM", new IfcInternalOrExternalEnumBuilder());
		this.put("IFCANGULARVELOCITYMEASURE", new IfcAngularVelocityMeasureBuilder());
		this.put("IFCFANTYPEENUM", new IfcFanTypeEnumBuilder());
		this.put("IFCLOGICALOPERATORENUM", new IfcLogicalOperatorEnumBuilder());
		this.put("IFCADDRESSTYPEENUM", new IfcAddressTypeEnumBuilder());
		this.put("IFCDISTRIBUTIONCHAMBERELEMENTTYPEENUM", new IfcDistributionChamberElementTypeEnumBuilder());
		this.put("IFCSOUNDSCALEENUM", new IfcSoundScaleEnumBuilder());
		this.put("IFCGEOMETRICPROJECTIONENUM", new IfcGeometricProjectionEnumBuilder());
		this.put("IFCSTAIRTYPEENUM", new IfcStairTypeEnumBuilder());
		this.put("IFCDUCTFITTINGTYPEENUM", new IfcDuctFittingTypeEnumBuilder());
		this.put("IFCLINEARVELOCITYMEASURE", new IfcLinearVelocityMeasureBuilder());
		this.put("IFCCOOLEDBEAMTYPEENUM", new IfcCooledBeamTypeEnumBuilder());
		this.put("IFCDESCRIPTIVEMEASURE", new IfcDescriptiveMeasureBuilder());
		this.put("IFCELECTRICAPPLIANCETYPEENUM", new IfcElectricApplianceTypeEnumBuilder());
		this.put("IFCSLABTYPEENUM", new IfcSlabTypeEnumBuilder());
		this.put("IFCVOLUMEMEASURE", new IfcVolumeMeasureBuilder());
		this.put("IFCPROJECTEDORTRUELENGTHENUM", new IfcProjectedOrTrueLengthEnumBuilder());
		this.put("IFCAREAMEASURE", new IfcAreaMeasureBuilder());
		this.put("IFCINTEGER", new IfcIntegerBuilder());
		this.put("IFCMASSPERLENGTHMEASURE", new IfcMassPerLengthMeasureBuilder());
		this.put("IFCDUCTSEGMENTTYPEENUM", new IfcDuctSegmentTypeEnumBuilder());
		this.put("IFCELECTRICCURRENTENUM", new IfcElectricCurrentEnumBuilder());
		this.put("IFCIDENTIFIER", new IfcIdentifierBuilder());
		this.put("IFCLINEARSTIFFNESSMEASURE", new IfcLinearStiffnessMeasureBuilder());
		this.put("IFCTIMESERIESDATATYPEENUM", new IfcTimeSeriesDataTypeEnumBuilder());
		this.put("IFCLIGHTDISTRIBUTIONCURVEENUM", new IfcLightDistributionCurveEnumBuilder());
		this.put("IFCLOADGROUPTYPEENUM", new IfcLoadGroupTypeEnumBuilder());
		this.put("IFCSPACETYPEENUM", new IfcSpaceTypeEnumBuilder());
		this.put("IFCSOUNDPOWERMEASURE", new IfcSoundPowerMeasureBuilder());
		this.put("IFCSHEARMODULUSMEASURE", new IfcShearModulusMeasureBuilder());
		this.put("IFCFOOTINGTYPEENUM", new IfcFootingTypeEnumBuilder());
		this.put("IFCMOISTUREDIFFUSIVITYMEASURE", new IfcMoistureDiffusivityMeasureBuilder());
		this.put("IFCAIRTERMINALBOXTYPEENUM", new IfcAirTerminalBoxTypeEnumBuilder());
		this.put("IFCSTRUCTURALSURFACETYPEENUM", new IfcStructuralSurfaceTypeEnumBuilder());
		this.put("IFCSTRUCTURALCURVETYPEENUM", new IfcStructuralCurveTypeEnumBuilder());
		this.put("IFCLUMINOUSINTENSITYDISTRIBUTIONMEASURE", new IfcLuminousIntensityDistributionMeasureBuilder());
		this.put("IFCDOORSTYLEOPERATIONENUM", new IfcDoorStyleOperationEnumBuilder());
		this.put("IFCMODULUSOFLINEARSUBGRADEREACTIONMEASURE", new IfcModulusOfLinearSubgradeReactionMeasureBuilder());
		this.put("IFCSPACEHEATERTYPEENUM", new IfcSpaceHeaterTypeEnumBuilder());
		this.put("IFCPIPEFITTINGTYPEENUM", new IfcPipeFittingTypeEnumBuilder());
		this.put("IFCBOOLEAN", new IfcBooleanBuilder());
		this.put("IFCTHERMALLOADTYPEENUM", new IfcThermalLoadTypeEnumBuilder());
		this.put("IFCSWITCHINGDEVICETYPEENUM", new IfcSwitchingDeviceTypeEnumBuilder());
		this.put("IFCPROCEDURETYPEENUM", new IfcProcedureTypeEnumBuilder());
		this.put("IFCTANKTYPEENUM", new IfcTankTypeEnumBuilder());
		this.put("IFCSECTIONALAREAINTEGRALMEASURE", new IfcSectionalAreaIntegralMeasureBuilder());
		this.put("IFCBEAMTYPEENUM", new IfcBeamTypeEnumBuilder());
		this.put("IFCMOTORCONNECTIONTYPEENUM", new IfcMotorConnectionTypeEnumBuilder());
		this.put("IFCDAMPERTYPEENUM", new IfcDamperTypeEnumBuilder());
		this.put("IFCUNITARYEQUIPMENTTYPEENUM", new IfcUnitaryEquipmentTypeEnumBuilder());
		this.put("IFCREINFORCINGBARSURFACEENUM", new IfcReinforcingBarSurfaceEnumBuilder());
		this.put("IFCCABLECARRIERSEGMENTTYPEENUM", new IfcCableCarrierSegmentTypeEnumBuilder());
		this.put("IFCPILECONSTRUCTIONENUM", new IfcPileConstructionEnumBuilder());
		this.put("IFCDIMENSIONCOUNT", new IfcDimensionCountBuilder());
		this.put("IFCCABLECARRIERFITTINGTYPEENUM", new IfcCableCarrierFittingTypeEnumBuilder());
		this.put("IFCPERMEABLECOVERINGOPERATIONENUM", new IfcPermeableCoveringOperationEnumBuilder());
		this.put("IFCDERIVEDUNITENUM", new IfcDerivedUnitEnumBuilder());
		this.put("IFCMOLECULARWEIGHTMEASURE", new IfcMolecularWeightMeasureBuilder());
		this.put("IFCENERGYMEASURE", new IfcEnergyMeasureBuilder());
		this.put("IFCPIPESEGMENTTYPEENUM", new IfcPipeSegmentTypeEnumBuilder());
		this.put("IFCGLOBALLYUNIQUEID", new IfcGloballyUniqueIdBuilder());
		this.put("IFCTEXTALIGNMENT", new IfcTextAlignmentBuilder());
		this.put("IFCTHERMALADMITTANCEMEASURE", new IfcThermalAdmittanceMeasureBuilder());
		this.put("IFCISOTHERMALMOISTURECAPACITYMEASURE", new IfcIsothermalMoistureCapacityMeasureBuilder());
		this.put("IFCAIRTERMINALTYPEENUM", new IfcAirTerminalTypeEnumBuilder());
		this.put("IFCHEATEXCHANGERTYPEENUM", new IfcHeatExchangerTypeEnumBuilder());
		this.put("IFCINDUCTANCEMEASURE", new IfcInductanceMeasureBuilder());
		this.put("IFCNUMERICMEASURE", new IfcNumericMeasureBuilder());
		this.put("IFCCONSTRAINTENUM", new IfcConstraintEnumBuilder());
		this.put("IFCFLOWDIRECTIONENUM", new IfcFlowDirectionEnumBuilder());
		this.put("IFCAMOUNTOFSUBSTANCEMEASURE", new IfcAmountOfSubstanceMeasureBuilder());
		this.put("IFCPOWERMEASURE", new IfcPowerMeasureBuilder());
		this.put("IFCCOILTYPEENUM", new IfcCoilTypeEnumBuilder());
		this.put("IFCPROTECTIVEDEVICETYPEENUM", new IfcProtectiveDeviceTypeEnumBuilder());
		this.put("IFCNULLSTYLE", new IfcNullStyleBuilder());
		this.put("IFCANALYSISMODELTYPEENUM", new IfcAnalysisModelTypeEnumBuilder());
		this.put("IFCSENSORTYPEENUM", new IfcSensorTypeEnumBuilder());
		this.put("IFCALARMTYPEENUM", new IfcAlarmTypeEnumBuilder());
		this.put("IFCOUTLETTYPEENUM", new IfcOutletTypeEnumBuilder());
		this.put("IFCRESOURCECONSUMPTIONENUM", new IfcResourceConsumptionEnumBuilder());
		this.put("IFCDOSEEQUIVALENTMEASURE", new IfcDoseEquivalentMeasureBuilder());
		this.put("IFCHEATINGVALUEMEASURE", new IfcHeatingValueMeasureBuilder());
		this.put("IFCINVENTORYTYPEENUM", new IfcInventoryTypeEnumBuilder());
		this.put("IFCCOVERINGTYPEENUM", new IfcCoveringTypeEnumBuilder());
		this.put("IFCRAILINGTYPEENUM", new IfcRailingTypeEnumBuilder());
		this.put("IFCREINFORCINGBARROLEENUM", new IfcReinforcingBarRoleEnumBuilder());
		this.put("IFCWARPINGCONSTANTMEASURE", new IfcWarpingConstantMeasureBuilder());
		this.put("IFCVALVETYPEENUM", new IfcValveTypeEnumBuilder());
		this.put("IFCPRESENTABLETEXT", new IfcPresentableTextBuilder());
		this.put("IFCTRANSPORTELEMENTTYPEENUM", new IfcTransportElementTypeEnumBuilder());
		this.put("IFCBOXALIGNMENT", new IfcBoxAlignmentBuilder());
		this.put("IFCSTACKTERMINALTYPEENUM", new IfcStackTerminalTypeEnumBuilder());
		this.put("IFCCOMPRESSORTYPEENUM", new IfcCompressorTypeEnumBuilder());
		this.put("IFCRAMPTYPEENUM", new IfcRampTypeEnumBuilder());
		this.put("IFCROOFTYPEENUM", new IfcRoofTypeEnumBuilder());
		this.put("IFCLINEARMOMENTMEASURE", new IfcLinearMomentMeasureBuilder());
		this.put("IFCPOSITIVEPLANEANGLEMEASURE", new IfcPositivePlaneAngleMeasureBuilder());
		this.put("IFCVIBRATIONISOLATORTYPEENUM", new IfcVibrationIsolatorTypeEnumBuilder());
		this.put("IFCAHEADORBEHIND", new IfcAheadOrBehindBuilder());
		this.put("IFCSECONDINMINUTE", new IfcSecondInMinuteBuilder());
		this.put("IFCPARAMETERVALUE", new IfcParameterValueBuilder());
		this.put("IFCELECTRICVOLTAGEMEASURE", new IfcElectricVoltageMeasureBuilder());
		this.put("IFCWINDOWPANELPOSITIONENUM", new IfcWindowPanelPositionEnumBuilder());
		this.put("IFCPLANARFORCEMEASURE", new IfcPlanarForceMeasureBuilder());
		this.put("IFCTHERMALEXPANSIONCOEFFICIENTMEASURE", new IfcThermalExpansionCoefficientMeasureBuilder());
		this.put("IFCLOGICAL", new IfcLogicalBuilder());
		this.put("IFCABSORBEDDOSEMEASURE", new IfcAbsorbedDoseMeasureBuilder());
		this.put("IFCCOLUMNTYPEENUM", new IfcColumnTypeEnumBuilder());
		this.put("IFCCOSTSCHEDULETYPEENUM", new IfcCostScheduleTypeEnumBuilder());
		this.put("IFCMODULUSOFELASTICITYMEASURE", new IfcModulusOfElasticityMeasureBuilder());
		this.put("IFCRIBPLATEDIRECTIONENUM", new IfcRibPlateDirectionEnumBuilder());
		this.put("IFCAIRTOAIRHEATRECOVERYTYPEENUM", new IfcAirToAirHeatRecoveryTypeEnumBuilder());
		this.put("IFCTHERMALTRANSMITTANCEMEASURE", new IfcThermalTransmittanceMeasureBuilder());
		this.put("IFCBOILERTYPEENUM", new IfcBoilerTypeEnumBuilder());
		this.put("IFCTEMPERATUREGRADIENTMEASURE", new IfcTemperatureGradientMeasureBuilder());
		this.put("IFCDOCUMENTCONFIDENTIALITYENUM", new IfcDocumentConfidentialityEnumBuilder());
		this.put("IFCELECTRICGENERATORTYPEENUM", new IfcElectricGeneratorTypeEnumBuilder());
		this.put("IFCMODULUSOFROTATIONALSUBGRADEREACTIONMEASURE", new IfcModulusOfRotationalSubgradeReactionMeasureBuilder());
		this.put("IFCOCCUPANTTYPEENUM", new IfcOccupantTypeEnumBuilder());
		this.put("IFCCONTEXTDEPENDENTMEASURE", new IfcContextDependentMeasureBuilder());
		this.put("IFCTEXTDECORATION", new IfcTextDecorationBuilder());
		this.put("IFCTRANSITIONCODE", new IfcTransitionCodeBuilder());
		this.put("IFCSTAIRFLIGHTTYPEENUM", new IfcStairFlightTypeEnumBuilder());
		this.put("IFCACTIONTYPEENUM", new IfcActionTypeEnumBuilder());
		this.put("IFCVAPORPERMEABILITYMEASURE", new IfcVaporPermeabilityMeasureBuilder());
		this.put("IFCLAYERSETDIRECTIONENUM", new IfcLayerSetDirectionEnumBuilder());
		this.put("IFCTENDONTYPEENUM", new IfcTendonTypeEnumBuilder());
		this.put("IFCCHILLERTYPEENUM", new IfcChillerTypeEnumBuilder());
		this.put("IFCSIUNIT", new IfcSIUnitBuilder());
		this.put("IFCRELASSIGNSTOACTOR", new IfcRelAssignsToActorBuilder());
		this.put("IFCDRAUGHTINGPREDEFINEDTEXTFONT", new IfcDraughtingPreDefinedTextFontBuilder());
		this.put("IFCDIRECTION", new IfcDirectionBuilder());
		this.put("IFCJUNCTIONBOXTYPE", new IfcJunctionBoxTypeBuilder());
		this.put("IFCFIRESUPPRESSIONTERMINALTYPE", new IfcFireSuppressionTerminalTypeBuilder());
		this.put("IFCRIGHTCIRCULARCONE", new IfcRightCircularConeBuilder());
		this.put("IFCREINFORCINGBAR", new IfcReinforcingBarBuilder());
		this.put("IFCCOORDINATEDUNIVERSALTIMEOFFSET", new IfcCoordinatedUniversalTimeOffsetBuilder());
		this.put("IFCFLOWSTORAGEDEVICE", new IfcFlowStorageDeviceBuilder());
		this.put("IFCDUCTSEGMENTTYPE", new IfcDuctSegmentTypeBuilder());
		this.put("IFCSTRUCTURALSURFACEMEMBER", new IfcStructuralSurfaceMemberBuilder());
		this.put("IFCTYPEOBJECT", new IfcTypeObjectBuilder());
		this.put("IFCFACETEDBREPWITHVOIDS", new IfcFacetedBrepWithVoidsBuilder());
		this.put("IFCMATERIALLAYER", new IfcMaterialLayerBuilder());
		this.put("IFCFACESURFACE", new IfcFaceSurfaceBuilder());
		this.put("IFCCIRCLEPROFILEDEF", new IfcCircleProfileDefBuilder());
		this.put("IFCRELASSOCIATESPROFILEPROPERTIES", new IfcRelAssociatesProfilePropertiesBuilder());
		this.put("IFCFANTYPE", new IfcFanTypeBuilder());
		this.put("IFCGEOMETRICCURVESET", new IfcGeometricCurveSetBuilder());
		this.put("IFCEVAPORATIVECOOLERTYPE", new IfcEvaporativeCoolerTypeBuilder());
		this.put("IFCORDERACTION", new IfcOrderActionBuilder());
		this.put("FILE_DESCRIPTION", new File_DescriptionBuilder());
		this.put("IFCRATIONALBEZIERCURVE", new IfcRationalBezierCurveBuilder());
		this.put("IFCFLOWFITTING", new IfcFlowFittingBuilder());
		this.put("IFCELLIPSE", new IfcEllipseBuilder());
		this.put("IFCWATERPROPERTIES", new IfcWaterPropertiesBuilder());
		this.put("IFCPIPEFITTINGTYPE", new IfcPipeFittingTypeBuilder());
		this.put("IFCENERGYCONVERSIONDEVICE", new IfcEnergyConversionDeviceBuilder());
		this.put("IFCPOLYGONALBOUNDEDHALFSPACE", new IfcPolygonalBoundedHalfSpaceBuilder());
		this.put("IFCSECTIONEDSPINE", new IfcSectionedSpineBuilder());
		this.put("IFCVIRTUALGRIDINTERSECTION", new IfcVirtualGridIntersectionBuilder());
		this.put("IFCGROUP", new IfcGroupBuilder());
		this.put("IFCLIGHTSOURCEDIRECTIONAL", new IfcLightSourceDirectionalBuilder());
		this.put("IFCPOLYLOOP", new IfcPolyLoopBuilder());
		this.put("IFCACTIONREQUEST", new IfcActionRequestBuilder());
		this.put("IFCRELASSIGNSTORESOURCE", new IfcRelAssignsToResourceBuilder());
		this.put("IFCRECTANGULARTRIMMEDSURFACE", new IfcRectangularTrimmedSurfaceBuilder());
		this.put("IFCDRAUGHTINGPREDEFINEDCOLOUR", new IfcDraughtingPreDefinedColourBuilder());
		this.put("IFCLOCALTIME", new IfcLocalTimeBuilder());
		this.put("IFCEDGELOOP", new IfcEdgeLoopBuilder());
		this.put("IFCSURFACESTYLESHADING", new IfcSurfaceStyleShadingBuilder());
		this.put("IFCDIMENSIONCURVETERMINATOR", new IfcDimensionCurveTerminatorBuilder());
		this.put("IFCCONNECTIONPOINTECCENTRICITY", new IfcConnectionPointEccentricityBuilder());
		this.put("IFCCOMPLEXPROPERTY", new IfcComplexPropertyBuilder());
		this.put("IFCCONSTRAINTCLASSIFICATIONRELATIONSHIP", new IfcConstraintClassificationRelationshipBuilder());
		this.put("IFCORGANIZATION", new IfcOrganizationBuilder());
		this.put("IFCQUANTITYTIME", new IfcQuantityTimeBuilder());
		this.put("IFCAPPLICATION", new IfcApplicationBuilder());
		this.put("IFCOPENSHELL", new IfcOpenShellBuilder());
		this.put("IFCTIMESERIESVALUE", new IfcTimeSeriesValueBuilder());
		this.put("IFCHEATEXCHANGERTYPE", new IfcHeatExchangerTypeBuilder());
		this.put("IFCHYGROSCOPICMATERIALPROPERTIES", new IfcHygroscopicMaterialPropertiesBuilder());
		this.put("IFCRELCONNECTSWITHECCENTRICITY", new IfcRelConnectsWithEccentricityBuilder());
		this.put("IFCCOLUMNTYPE", new IfcColumnTypeBuilder());
		this.put("IFCPRODUCTDEFINITIONSHAPE", new IfcProductDefinitionShapeBuilder());
		this.put("IFCBLOBTEXTURE", new IfcBlobTextureBuilder());
		this.put("IFCSTRUCTURALSTEELPROFILEPROPERTIES", new IfcStructuralSteelProfilePropertiesBuilder());
		this.put("IFCACTORROLE", new IfcActorRoleBuilder());
		this.put("IFCTABLEROW", new IfcTableRowBuilder());
		this.put("IFCVERTEXPOINT", new IfcVertexPointBuilder());
		this.put("IFCPRESENTATIONSTYLEASSIGNMENT", new IfcPresentationStyleAssignmentBuilder());
		this.put("IFCCURVESTYLEFONT", new IfcCurveStyleFontBuilder());
		this.put("IFCMAPPEDITEM", new IfcMappedItemBuilder());
		this.put("IFCCONSTRUCTIONPRODUCTRESOURCE", new IfcConstructionProductResourceBuilder());
		this.put("IFCPLANAREXTENT", new IfcPlanarExtentBuilder());
		this.put("IFCDIMENSIONCURVE", new IfcDimensionCurveBuilder());
		this.put("IFCMEMBER", new IfcMemberBuilder());
		this.put("IFCDAMPERTYPE", new IfcDamperTypeBuilder());
		this.put("IFCLSHAPEPROFILEDEF", new IfcLShapeProfileDefBuilder());
		this.put("IFCRELSPACEBOUNDARY", new IfcRelSpaceBoundaryBuilder());
		this.put("IFCTEXTSTYLEFORDEFINEDFONT", new IfcTextStyleForDefinedFontBuilder());
		this.put("IFCMATERIALLAYERSET", new IfcMaterialLayerSetBuilder());
		this.put("IFCELECTRICDISTRIBUTIONPOINT", new IfcElectricDistributionPointBuilder());
		this.put("IFCMATERIALCLASSIFICATIONRELATIONSHIP", new IfcMaterialClassificationRelationshipBuilder());
		this.put("IFCCONDENSERTYPE", new IfcCondenserTypeBuilder());
		this.put("IFCCHILLERTYPE", new IfcChillerTypeBuilder());
		this.put("IFCMATERIALLAYERSETUSAGE", new IfcMaterialLayerSetUsageBuilder());
		this.put("IFCIMAGETEXTURE", new IfcImageTextureBuilder());
		this.put("IFCVIRTUALELEMENT", new IfcVirtualElementBuilder());
		this.put("IFCSURFACESTYLEWITHTEXTURES", new IfcSurfaceStyleWithTexturesBuilder());
		this.put("IFCACTOR", new IfcActorBuilder());
		this.put("IFCRELCONTAINEDINSPATIALSTRUCTURE", new IfcRelContainedInSpatialStructureBuilder());
		this.put("IFCCIRCLEHOLLOWPROFILEDEF", new IfcCircleHollowProfileDefBuilder());
		this.put("IFCTERMINATORSYMBOL", new IfcTerminatorSymbolBuilder());
		this.put("IFCCONSTRUCTIONEQUIPMENTRESOURCE", new IfcConstructionEquipmentResourceBuilder());
		this.put("IFCPERMIT", new IfcPermitBuilder());
		this.put("IFCSTYLEDITEM", new IfcStyledItemBuilder());
		this.put("IFCORGANIZATIONRELATIONSHIP", new IfcOrganizationRelationshipBuilder());
		this.put("IFCDOCUMENTELECTRONICFORMAT", new IfcDocumentElectronicFormatBuilder());
		this.put("IFCALARMTYPE", new IfcAlarmTypeBuilder());
		this.put("IFCRELASSIGNSTOPRODUCT", new IfcRelAssignsToProductBuilder());
		this.put("IFCPROPERTYENUMERATION", new IfcPropertyEnumerationBuilder());
		this.put("IFCDISCRETEACCESSORYTYPE", new IfcDiscreteAccessoryTypeBuilder());
		this.put("IFCFACE", new IfcFaceBuilder());
		this.put("IFCRELASSIGNSTOGROUP", new IfcRelAssignsToGroupBuilder());
		this.put("IFCTUBEBUNDLETYPE", new IfcTubeBundleTypeBuilder());
		this.put("IFCASSET", new IfcAssetBuilder());
		this.put("IFCSURFACESTYLERENDERING", new IfcSurfaceStyleRenderingBuilder());
		this.put("IFCPROCEDURE", new IfcProcedureBuilder());
		this.put("IFCCURRENCYRELATIONSHIP", new IfcCurrencyRelationshipBuilder());
		this.put("IFCDOOR", new IfcDoorBuilder());
		this.put("IFCVERTEXBASEDTEXTUREMAP", new IfcVertexBasedTextureMapBuilder());
		this.put("IFCSPACETYPE", new IfcSpaceTypeBuilder());
		this.put("IFCMECHANICALMATERIALPROPERTIES", new IfcMechanicalMaterialPropertiesBuilder());
		this.put("IFCPOINTONCURVE", new IfcPointOnCurveBuilder());
		this.put("IFCCONDITION", new IfcConditionBuilder());
		this.put("IFCSTRUCTURALPOINTACTION", new IfcStructuralPointActionBuilder());
		this.put("IFCRELCONNECTSSTRUCTURALMEMBER", new IfcRelConnectsStructuralMemberBuilder());
		this.put("IFCANNOTATIONFILLAREAOCCURRENCE", new IfcAnnotationFillAreaOccurrenceBuilder());
		this.put("IFCFACEBASEDSURFACEMODEL", new IfcFaceBasedSurfaceModelBuilder());
		this.put("IFCRAMPFLIGHT", new IfcRampFlightBuilder());
		this.put("IFCOWNERHISTORY", new IfcOwnerHistoryBuilder());
		this.put("IFCDOCUMENTINFORMATIONRELATIONSHIP", new IfcDocumentInformationRelationshipBuilder());
		this.put("FILE_NAME", new File_NameBuilder());
		this.put("IFCRAMPFLIGHTTYPE", new IfcRampFlightTypeBuilder());
		this.put("IFCDIMENSIONALEXPONENTS", new IfcDimensionalExponentsBuilder());
		this.put("IFCRELVOIDSELEMENT", new IfcRelVoidsElementBuilder());
		this.put("IFCDERIVEDUNIT", new IfcDerivedUnitBuilder());
		this.put("IFCSTRUCTURALCURVEMEMBERVARYING", new IfcStructuralCurveMemberVaryingBuilder());
		this.put("IFCRELASSOCIATESAPPLIEDVALUE", new IfcRelAssociatesAppliedValueBuilder());
		this.put("IFCPROJECTORDERRECORD", new IfcProjectOrderRecordBuilder());
		this.put("IFCSERVICELIFE", new IfcServiceLifeBuilder());
		this.put("IFCQUANTITYWEIGHT", new IfcQuantityWeightBuilder());
		this.put("IFCCURVESTYLEFONTANDSCALING", new IfcCurveStyleFontAndScalingBuilder());
		this.put("IFCPROPERTYREFERENCEVALUE", new IfcPropertyReferenceValueBuilder());
		this.put("IFCELEMENTASSEMBLY", new IfcElementAssemblyBuilder());
		this.put("IFCFACEOUTERBOUND", new IfcFaceOuterBoundBuilder());
		this.put("IFCPREDEFINEDPOINTMARKERSYMBOL", new IfcPreDefinedPointMarkerSymbolBuilder());
		this.put("IFCOPENINGELEMENT", new IfcOpeningElementBuilder());
		this.put("IFCLIGHTSOURCEPOSITIONAL", new IfcLightSourcePositionalBuilder());
		this.put("IFCRELDEFINESBYTYPE", new IfcRelDefinesByTypeBuilder());
		this.put("IFCSUBCONTRACTRESOURCE", new IfcSubContractResourceBuilder());
		this.put("IFCCLASSIFICATIONITEM", new IfcClassificationItemBuilder());
		this.put("IFCOCCUPANT", new IfcOccupantBuilder());
		this.put("IFCSURFACESTYLE", new IfcSurfaceStyleBuilder());
		this.put("IFCLABORRESOURCE", new IfcLaborResourceBuilder());
		this.put("IFCRELASSOCIATESCONSTRAINT", new IfcRelAssociatesConstraintBuilder());
		this.put("IFCTEXTSTYLEWITHBOXCHARACTERISTICS", new IfcTextStyleWithBoxCharacteristicsBuilder());
		this.put("IFCCOSTITEM", new IfcCostItemBuilder());
		this.put("IFCFURNISHINGELEMENTTYPE", new IfcFurnishingElementTypeBuilder());
		this.put("IFCSPHERE", new IfcSphereBuilder());
		this.put("IFCBUILDINGELEMENTPROXY", new IfcBuildingElementProxyBuilder());
		this.put("IFCRELASSIGNSTASKS", new IfcRelAssignsTasksBuilder());
		this.put("IFCEQUIPMENTSTANDARD", new IfcEquipmentStandardBuilder());
		this.put("IFCBOUNDINGBOX", new IfcBoundingBoxBuilder());
		this.put("IFCLIBRARYINFORMATION", new IfcLibraryInformationBuilder());
		this.put("IFCVERTEXLOOP", new IfcVertexLoopBuilder());
		this.put("IFCRADIUSDIMENSION", new IfcRadiusDimensionBuilder());
		this.put("IFCPROPERTYCONSTRAINTRELATIONSHIP", new IfcPropertyConstraintRelationshipBuilder());
		this.put("IFCBOXEDHALFSPACE", new IfcBoxedHalfSpaceBuilder());
		this.put("IFCLIGHTSOURCEGONIOMETRIC", new IfcLightSourceGoniometricBuilder());
		this.put("IFCTENDONANCHOR", new IfcTendonAnchorBuilder());
		this.put("IFCCONTROLLERTYPE", new IfcControllerTypeBuilder());
		this.put("IFCTRANSFORMERTYPE", new IfcTransformerTypeBuilder());
		this.put("IFCPREDEFINEDDIMENSIONSYMBOL", new IfcPreDefinedDimensionSymbolBuilder());
		this.put("IFCCSHAPEPROFILEDEF", new IfcCShapeProfileDefBuilder());
		this.put("IFCTEXTUREMAP", new IfcTextureMapBuilder());
		this.put("IFCFUELPROPERTIES", new IfcFuelPropertiesBuilder());
		this.put("IFCCABLECARRIERSEGMENTTYPE", new IfcCableCarrierSegmentTypeBuilder());
		this.put("IFCSPACETHERMALLOADPROPERTIES", new IfcSpaceThermalLoadPropertiesBuilder());
		this.put("IFCRELASSIGNSTOCONTROL", new IfcRelAssignsToControlBuilder());
		this.put("IFCELECTRICALBASEPROPERTIES", new IfcElectricalBasePropertiesBuilder());
		this.put("IFCTASK", new IfcTaskBuilder());
		this.put("IFCSUBEDGE", new IfcSubedgeBuilder());
		this.put("IFCDISTRIBUTIONCHAMBERELEMENTTYPE", new IfcDistributionChamberElementTypeBuilder());
		this.put("IFCPLATE", new IfcPlateBuilder());
		this.put("IFCQUANTITYCOUNT", new IfcQuantityCountBuilder());
		this.put("IFCMOVE", new IfcMoveBuilder());
		this.put("IFCRELASSOCIATESAPPROVAL", new IfcRelAssociatesApprovalBuilder());
		this.put("IFCFLOWTREATMENTDEVICE", new IfcFlowTreatmentDeviceBuilder());
		this.put("IFCCOVERINGTYPE", new IfcCoveringTypeBuilder());
		this.put("IFCLINE", new IfcLineBuilder());
		this.put("IFCSTRUCTURALLOADPLANARFORCE", new IfcStructuralLoadPlanarForceBuilder());
		this.put("IFCSTAIRFLIGHT", new IfcStairFlightBuilder());
		this.put("IFCSANITARYTERMINALTYPE", new IfcSanitaryTerminalTypeBuilder());
		this.put("IFCRELCONNECTSSTRUCTURALELEMENT", new IfcRelConnectsStructuralElementBuilder());
		this.put("IFCLIGHTDISTRIBUTIONDATA", new IfcLightDistributionDataBuilder());
		this.put("IFCWASTETERMINALTYPE", new IfcWasteTerminalTypeBuilder());
		this.put("IFCSLAB", new IfcSlabBuilder());
		this.put("IFCFILLAREASTYLETILES", new IfcFillAreaStyleTilesBuilder());
		this.put("IFCEQUIPMENTELEMENT", new IfcEquipmentElementBuilder());
		this.put("IFCDUCTFITTINGTYPE", new IfcDuctFittingTypeBuilder());
		this.put("IFCSURFACESTYLELIGHTING", new IfcSurfaceStyleLightingBuilder());
		this.put("IFCSITE", new IfcSiteBuilder());
		this.put("IFCTRIMMEDCURVE", new IfcTrimmedCurveBuilder());
		this.put("IFCREPRESENTATIONCONTEXT", new IfcRepresentationContextBuilder());
		this.put("IFCCOLOURRGB", new IfcColourRgbBuilder());
		this.put("IFCRELASSOCIATESMATERIAL", new IfcRelAssociatesMaterialBuilder());
		this.put("IFCORIENTEDEDGE", new IfcOrientedEdgeBuilder());
		this.put("IFCELECTRICGENERATORTYPE", new IfcElectricGeneratorTypeBuilder());
		this.put("IFCRELASSOCIATESCLASSIFICATION", new IfcRelAssociatesClassificationBuilder());
		this.put("IFCDUCTSILENCERTYPE", new IfcDuctSilencerTypeBuilder());
		this.put("IFCDIMENSIONPAIR", new IfcDimensionPairBuilder());
		this.put("IFCENERGYPROPERTIES", new IfcEnergyPropertiesBuilder());
		this.put("IFCPILE", new IfcPileBuilder());
		this.put("IFCCRANERAILFSHAPEPROFILEDEF", new IfcCraneRailFShapeProfileDefBuilder());
		this.put("IFCMATERIAL", new IfcMaterialBuilder());
		this.put("IFCCIRCLE", new IfcCircleBuilder());
		this.put("IFCSTRUCTURALLOADSINGLEFORCEWARPING", new IfcStructuralLoadSingleForceWarpingBuilder());
		this.put("IFCWALLSTANDARDCASE", new IfcWallStandardCaseBuilder());
		this.put("IFCARBITRARYPROFILEDEFWITHVOIDS", new IfcArbitraryProfileDefWithVoidsBuilder());
		this.put("IFCTEXTSTYLE", new IfcTextStyleBuilder());
		this.put("IFCMECHANICALSTEELMATERIALPROPERTIES", new IfcMechanicalSteelMaterialPropertiesBuilder());
		this.put("IFCBEAM", new IfcBeamBuilder());
		this.put("IFCOFFSETCURVE3D", new IfcOffsetCurve3DBuilder());
		this.put("IFCAPPROVAL", new IfcApprovalBuilder());
		this.put("IFCPRODUCTSOFCOMBUSTIONPROPERTIES", new IfcProductsOfCombustionPropertiesBuilder());
		this.put("IFCCURTAINWALL", new IfcCurtainWallBuilder());
		this.put("IFCDISTRIBUTIONELEMENTTYPE", new IfcDistributionElementTypeBuilder());
		this.put("IFCISHAPEPROFILEDEF", new IfcIShapeProfileDefBuilder());
		this.put("IFCARBITRARYOPENPROFILEDEF", new IfcArbitraryOpenProfileDefBuilder());
		this.put("IFCVALVETYPE", new IfcValveTypeBuilder());
		this.put("IFCEXTRUDEDAREASOLID", new IfcExtrudedAreaSolidBuilder());
		this.put("IFCACTUATORTYPE", new IfcActuatorTypeBuilder());
		this.put("IFCTEXTURECOORDINATEGENERATOR", new IfcTextureCoordinateGeneratorBuilder());
		this.put("IFCPROPERTYSET", new IfcPropertySetBuilder());
		this.put("IFCPROTECTIVEDEVICETYPE", new IfcProtectiveDeviceTypeBuilder());
		this.put("IFCCONTEXTDEPENDENTUNIT", new IfcContextDependentUnitBuilder());
		this.put("IFCCONSTRAINTRELATIONSHIP", new IfcConstraintRelationshipBuilder());
		this.put("IFCOFFSETCURVE2D", new IfcOffsetCurve2DBuilder());
		this.put("IFCBOUNDARYNODECONDITIONWARPING", new IfcBoundaryNodeConditionWarpingBuilder());
		this.put("IFCFURNITURESTANDARD", new IfcFurnitureStandardBuilder());
		this.put("IFCVERTEX", new IfcVertexBuilder());
		this.put("IFCREINFORCINGMESH", new IfcReinforcingMeshBuilder());
		this.put("IFCZONE", new IfcZoneBuilder());
		this.put("IFCRAMP", new IfcRampBuilder());
		this.put("IFCSTAIRFLIGHTTYPE", new IfcStairFlightTypeBuilder());
		this.put("IFCMEMBERTYPE", new IfcMemberTypeBuilder());
		this.put("IFCTEXTUREVERTEX", new IfcTextureVertexBuilder());
		this.put("IFCTANKTYPE", new IfcTankTypeBuilder());
		this.put("IFCCURVEBOUNDEDPLANE", new IfcCurveBoundedPlaneBuilder());
		this.put("IFCEDGE", new IfcEdgeBuilder());
		this.put("IFCANNOTATIONFILLAREA", new IfcAnnotationFillAreaBuilder());
		this.put("IFCSERVICELIFEFACTOR", new IfcServiceLifeFactorBuilder());
		this.put("IFCMEASUREWITHUNIT", new IfcMeasureWithUnitBuilder());
		this.put("IFCMONETARYUNIT", new IfcMonetaryUnitBuilder());
		this.put("IFCTEXTLITERALWITHEXTENT", new IfcTextLiteralWithExtentBuilder());
		this.put("IFCQUANTITYAREA", new IfcQuantityAreaBuilder());
		this.put("IFCUNITASSIGNMENT", new IfcUnitAssignmentBuilder());
		this.put("IFCRELSCHEDULESCOSTITEMS", new IfcRelSchedulesCostItemsBuilder());
		this.put("IFCVIBRATIONISOLATORTYPE", new IfcVibrationIsolatorTypeBuilder());
		this.put("IFCRELAGGREGATES", new IfcRelAggregatesBuilder());
		this.put("IFCSCHEDULETIMECONTROL", new IfcScheduleTimeControlBuilder());
		this.put("IFCCOMPOSITECURVE", new IfcCompositeCurveBuilder());
		this.put("IFCRELASSIGNSTOPROCESS", new IfcRelAssignsToProcessBuilder());
		this.put("IFCDRAUGHTINGCALLOUT", new IfcDraughtingCalloutBuilder());
		this.put("IFCRELCONNECTSELEMENTS", new IfcRelConnectsElementsBuilder());
		this.put("IFCTEXTSTYLEFONTMODEL", new IfcTextStyleFontModelBuilder());
		this.put("IFCPROPERTYENUMERATEDVALUE", new IfcPropertyEnumeratedValueBuilder());
		this.put("IFCCARTESIANPOINT", new IfcCartesianPointBuilder());
		this.put("IFCBUILDING", new IfcBuildingBuilder());
		this.put("IFCELLIPSEPROFILEDEF", new IfcEllipseProfileDefBuilder());
		this.put("IFCCONVERSIONBASEDUNIT", new IfcConversionBasedUnitBuilder());
		this.put("IFCSHELLBASEDSURFACEMODEL", new IfcShellBasedSurfaceModelBuilder());
		this.put("IFCANNOTATIONCURVEOCCURRENCE", new IfcAnnotationCurveOccurrenceBuilder());
		this.put("IFCAIRTERMINALBOXTYPE", new IfcAirTerminalBoxTypeBuilder());
		this.put("IFCSWITCHINGDEVICETYPE", new IfcSwitchingDeviceTypeBuilder());
		this.put("IFCPLATETYPE", new IfcPlateTypeBuilder());
		this.put("IFCPROJECTORDER", new IfcProjectOrderBuilder());
		this.put("IFCDOORSTYLE", new IfcDoorStyleBuilder());
		this.put("IFCEXTERNALLYDEFINEDSURFACESTYLE", new IfcExternallyDefinedSurfaceStyleBuilder());
		this.put("IFCSPACE", new IfcSpaceBuilder());
		this.put("IFCPLANE", new IfcPlaneBuilder());
		this.put("IFCSURFACESTYLEREFRACTION", new IfcSurfaceStyleRefractionBuilder());
		this.put("IFCBOUNDARYFACECONDITION", new IfcBoundaryFaceConditionBuilder());
		this.put("IFCFLOWSEGMENT", new IfcFlowSegmentBuilder());
		this.put("IFCTRANSPORTELEMENTTYPE", new IfcTransportElementTypeBuilder());
		this.put("IFCSURFACEOFLINEAREXTRUSION", new IfcSurfaceOfLinearExtrusionBuilder());
		this.put("IFCRECTANGLEHOLLOWPROFILEDEF", new IfcRectangleHollowProfileDefBuilder());
		this.put("IFCSTACKTERMINALTYPE", new IfcStackTerminalTypeBuilder());
		this.put("IFCDISTRIBUTIONELEMENT", new IfcDistributionElementBuilder());
		this.put("IFCLOCALPLACEMENT", new IfcLocalPlacementBuilder());
		this.put("IFCUNITARYEQUIPMENTTYPE", new IfcUnitaryEquipmentTypeBuilder());
		this.put("IFCBEZIERCURVE", new IfcBezierCurveBuilder());
		this.put("IFCEXTERNALLYDEFINEDTEXTFONT", new IfcExternallyDefinedTextFontBuilder());
		this.put("IFCFILTERTYPE", new IfcFilterTypeBuilder());
		this.put("IFCSTRUCTURALPOINTCONNECTION", new IfcStructuralPointConnectionBuilder());
		this.put("IFCDEFINEDSYMBOL", new IfcDefinedSymbolBuilder());
		this.put("IFCCOSTSCHEDULE", new IfcCostScheduleBuilder());
		this.put("IFCBOUNDARYNODECONDITION", new IfcBoundaryNodeConditionBuilder());
		this.put("IFCRAILING", new IfcRailingBuilder());
		this.put("IFCCARTESIANTRANSFORMATIONOPERATOR3DNONUNIFORM", new IfcCartesianTransformationOperator3DnonUniformBuilder());
		this.put("IFCQUANTITYVOLUME", new IfcQuantityVolumeBuilder());
		this.put("IFCREPRESENTATIONMAP", new IfcRepresentationMapBuilder());
		this.put("IFCELECTRICMOTORTYPE", new IfcElectricMotorTypeBuilder());
		this.put("IFCLIGHTSOURCEAMBIENT", new IfcLightSourceAmbientBuilder());
		this.put("IFCTEXTLITERAL", new IfcTextLiteralBuilder());
		this.put("FILE_SCHEMA", new File_SchemaBuilder());
		this.put("IFCSTRUCTURALCURVECONNECTION", new IfcStructuralCurveConnectionBuilder());
		this.put("IFCELECTRICHEATERTYPE", new IfcElectricHeaterTypeBuilder());
		this.put("IFCELEMENTQUANTITY", new IfcElementQuantityBuilder());
		this.put("IFCREGULARTIMESERIES", new IfcRegularTimeSeriesBuilder());
		this.put("IFCGRIDAXIS", new IfcGridAxisBuilder());
		this.put("IFCCRANERAILASHAPEPROFILEDEF", new IfcCraneRailAShapeProfileDefBuilder());
		this.put("IFCTENDON", new IfcTendonBuilder());
		this.put("IFCROUNDEDEDGEFEATURE", new IfcRoundedEdgeFeatureBuilder());
		this.put("IFCWALLTYPE", new IfcWallTypeBuilder());
		this.put("IFCBOUNDEDSURFACE", new IfcBoundedSurfaceBuilder());
		this.put("IFCPERSON", new IfcPersonBuilder());
		this.put("IFCWORKPLAN", new IfcWorkPlanBuilder());
		this.put("IFCBOOLEANRESULT", new IfcBooleanResultBuilder());
		this.put("IFCFASTENERTYPE", new IfcFastenerTypeBuilder());
		this.put("IFCTELECOMADDRESS", new IfcTelecomAddressBuilder());
		this.put("IFCANNOTATIONTEXTOCCURRENCE", new IfcAnnotationTextOccurrenceBuilder());
		this.put("IFCPROPERTYTABLEVALUE", new IfcPropertyTableValueBuilder());
		this.put("IFCIRREGULARTIMESERIESVALUE", new IfcIrregularTimeSeriesValueBuilder());
		this.put("IFCDOCUMENTINFORMATION", new IfcDocumentInformationBuilder());
		this.put("IFCPIPESEGMENTTYPE", new IfcPipeSegmentTypeBuilder());
		this.put("IFCRELINTERACTIONREQUIREMENTS", new IfcRelInteractionRequirementsBuilder());
		this.put("IFCREINFORCEMENTBARPROPERTIES", new IfcReinforcementBarPropertiesBuilder());
		this.put("IFCCONDITIONCRITERION", new IfcConditionCriterionBuilder());
		this.put("IFCSTRUCTURALPLANARACTIONVARYING", new IfcStructuralPlanarActionVaryingBuilder());
		this.put("IFCARBITRARYCLOSEDPROFILEDEF", new IfcArbitraryClosedProfileDefBuilder());
		this.put("IFCCLASSIFICATIONREFERENCE", new IfcClassificationReferenceBuilder());
		this.put("IFCMOTORCONNECTIONTYPE", new IfcMotorConnectionTypeBuilder());
		this.put("IFCPERMEABLECOVERINGPROPERTIES", new IfcPermeableCoveringPropertiesBuilder());
		this.put("IFCWINDOW", new IfcWindowBuilder());
		this.put("IFCAXIS1PLACEMENT", new IfcAxis1PlacementBuilder());
		this.put("IFCDERIVEDUNITELEMENT", new IfcDerivedUnitElementBuilder());
		this.put("IFCRIBPLATEPROFILEPROPERTIES", new IfcRibPlateProfilePropertiesBuilder());
		this.put("IFCRELSERVICESBUILDINGS", new IfcRelServicesBuildingsBuilder());
		this.put("IFCBOUNDARYEDGECONDITION", new IfcBoundaryEdgeConditionBuilder());
		this.put("IFCENVIRONMENTALIMPACTVALUE", new IfcEnvironmentalImpactValueBuilder());
		this.put("IFCCSGSOLID", new IfcCsgSolidBuilder());
		this.put("IFCCABLESEGMENTTYPE", new IfcCableSegmentTypeBuilder());
		this.put("IFCAPPROVALACTORRELATIONSHIP", new IfcApprovalActorRelationshipBuilder());
		this.put("IFCFACETEDBREP", new IfcFacetedBrepBuilder());
		this.put("IFCCALENDARDATE", new IfcCalendarDateBuilder());
		this.put("IFCONEDIRECTIONREPEATFACTOR", new IfcOneDirectionRepeatFactorBuilder());
		this.put("IFCRELCONNECTSPORTTOELEMENT", new IfcRelConnectsPortToElementBuilder());
		this.put("IFCREINFORCEMENTDEFINITIONPROPERTIES", new IfcReinforcementDefinitionPropertiesBuilder());
		this.put("IFCDERIVEDPROFILEDEF", new IfcDerivedProfileDefBuilder());
		this.put("IFCRELCOVERSBLDGELEMENTS", new IfcRelCoversBldgElementsBuilder());
		this.put("IFCRELASSOCIATES", new IfcRelAssociatesBuilder());
		this.put("IFCCLASSIFICATIONNOTATION", new IfcClassificationNotationBuilder());
		this.put("IFCPRESENTATIONLAYERASSIGNMENT", new IfcPresentationLayerAssignmentBuilder());
		this.put("IFCSTRUCTURALCURVEMEMBER", new IfcStructuralCurveMemberBuilder());
		this.put("IFCCONSTRAINTAGGREGATIONRELATIONSHIP", new IfcConstraintAggregationRelationshipBuilder());
		this.put("IFCSTRUCTURALLOADLINEARFORCE", new IfcStructuralLoadLinearForceBuilder());
		this.put("IFCSOUNDVALUE", new IfcSoundValueBuilder());
		this.put("IFCRECTANGULARPYRAMID", new IfcRectangularPyramidBuilder());
		this.put("IFCRELDEFINESBYPROPERTIES", new IfcRelDefinesByPropertiesBuilder());
		this.put("IFCPIXELTEXTURE", new IfcPixelTextureBuilder());
		this.put("IFCTEXTSTYLETEXTMODEL", new IfcTextStyleTextModelBuilder());
		this.put("IFCSTAIR", new IfcStairBuilder());
		this.put("IFCSHAPEREPRESENTATION", new IfcShapeRepresentationBuilder());
		this.put("IFCEDGECURVE", new IfcEdgeCurveBuilder());
		this.put("IFCCONNECTIONCURVEGEOMETRY", new IfcConnectionCurveGeometryBuilder());
		this.put("IFCPERFORMANCEHISTORY", new IfcPerformanceHistoryBuilder());
		this.put("IFCSTRUCTURALLOADSINGLEFORCE", new IfcStructuralLoadSingleForceBuilder());
		this.put("IFCMATERIALDEFINITIONREPRESENTATION", new IfcMaterialDefinitionRepresentationBuilder());
		this.put("IFCCOOLEDBEAMTYPE", new IfcCooledBeamTypeBuilder());
		this.put("IFCDIAMETERDIMENSION", new IfcDiameterDimensionBuilder());
		this.put("IFCTYPEPRODUCT", new IfcTypeProductBuilder());
		this.put("IFCSTRUCTURALLOADSINGLEDISPLACEMENT", new IfcStructuralLoadSingleDisplacementBuilder());
		this.put("IFCWINDOWPANELPROPERTIES", new IfcWindowPanelPropertiesBuilder());
		this.put("IFCRAILINGTYPE", new IfcRailingTypeBuilder());
		this.put("IFCELECTRICTIMECONTROLTYPE", new IfcElectricTimeControlTypeBuilder());
		this.put("IFCPREDEFINEDTERMINATORSYMBOL", new IfcPreDefinedTerminatorSymbolBuilder());
		this.put("IFCGEOMETRICSET", new IfcGeometricSetBuilder());
		this.put("IFCTHERMALMATERIALPROPERTIES", new IfcThermalMaterialPropertiesBuilder());
		this.put("IFCAIRTOAIRHEATRECOVERYTYPE", new IfcAirToAirHeatRecoveryTypeBuilder());
		this.put("IFCEVAPORATORTYPE", new IfcEvaporatorTypeBuilder());
		this.put("IFCCLASSIFICATIONNOTATIONFACET", new IfcClassificationNotationFacetBuilder());
		this.put("IFCPROPERTYLISTVALUE", new IfcPropertyListValueBuilder());
		this.put("IFCDOORLININGPROPERTIES", new IfcDoorLiningPropertiesBuilder());
		this.put("IFCBOOLEANCLIPPINGRESULT", new IfcBooleanClippingResultBuilder());
		this.put("IFCCENTERLINEPROFILEDEF", new IfcCenterLineProfileDefBuilder());
		this.put("IFCDRAUGHTINGPREDEFINEDCURVEFONT", new IfcDraughtingPreDefinedCurveFontBuilder());
		this.put("IFCCOVERING", new IfcCoveringBuilder());
		this.put("IFCPROPERTYDEPENDENCYRELATIONSHIP", new IfcPropertyDependencyRelationshipBuilder());
		this.put("IFCAPPROVALRELATIONSHIP", new IfcApprovalRelationshipBuilder());
		this.put("IFCREFERENCESVALUEDOCUMENT", new IfcReferencesValueDocumentBuilder());
		this.put("IFCSTRUCTURALRESULTGROUP", new IfcStructuralResultGroupBuilder());
		this.put("IFCAPPLIEDVALUERELATIONSHIP", new IfcAppliedValueRelationshipBuilder());
		this.put("IFCFAILURECONNECTIONCONDITION", new IfcFailureConnectionConditionBuilder());
		this.put("IFCBEAMTYPE", new IfcBeamTypeBuilder());
		this.put("IFCCOLUMN", new IfcColumnBuilder());
		this.put("IFCRECTANGLEPROFILEDEF", new IfcRectangleProfileDefBuilder());
		this.put("IFCCURVESTYLEFONTPATTERN", new IfcCurveStyleFontPatternBuilder());
		this.put("IFCLIGHTINTENSITYDISTRIBUTION", new IfcLightIntensityDistributionBuilder());
		this.put("IFCPOINTONSURFACE", new IfcPointOnSurfaceBuilder());
		this.put("IFCSTRUCTURALSURFACECONNECTION", new IfcStructuralSurfaceConnectionBuilder());
		this.put("IFCSTRUCTURALPOINTREACTION", new IfcStructuralPointReactionBuilder());
		this.put("IFCLAMPTYPE", new IfcLampTypeBuilder());
		this.put("IFCSPACEHEATERTYPE", new IfcSpaceHeaterTypeBuilder());
		this.put("IFCSTYLEDREPRESENTATION", new IfcStyledRepresentationBuilder());
		this.put("IFCSTRUCTUREDDIMENSIONCALLOUT", new IfcStructuredDimensionCalloutBuilder());
		this.put("IFCANNOTATIONSYMBOLOCCURRENCE", new IfcAnnotationSymbolOccurrenceBuilder());
		this.put("IFCPATH", new IfcPathBuilder());
		this.put("IFCSTRUCTURALPROFILEPROPERTIES", new IfcStructuralProfilePropertiesBuilder());
		this.put("IFCELECTRICAPPLIANCETYPE", new IfcElectricApplianceTypeBuilder());
		this.put("IFCPOLYLINE", new IfcPolylineBuilder());
		this.put("IFCPROXY", new IfcProxyBuilder());
		this.put("IFCEXTERNALLYDEFINEDHATCHSTYLE", new IfcExternallyDefinedHatchStyleBuilder());
		this.put("IFCTWODIRECTIONREPEATFACTOR", new IfcTwoDirectionRepeatFactorBuilder());
		this.put("IFCSECTIONPROPERTIES", new IfcSectionPropertiesBuilder());
		this.put("IFCELECTRICFLOWSTORAGEDEVICETYPE", new IfcElectricFlowStorageDeviceTypeBuilder());
		this.put("IFCGASTERMINALTYPE", new IfcGasTerminalTypeBuilder());
		this.put("IFCGEOMETRICREPRESENTATIONSUBCONTEXT", new IfcGeometricRepresentationSubContextBuilder());
		this.put("IFCSTRUCTURALLOADSINGLEDISPLACEMENTDISTORTION", new IfcStructuralLoadSingleDisplacementDistortionBuilder());
		this.put("IFCBOILERTYPE", new IfcBoilerTypeBuilder());
		this.put("IFCFACEBOUND", new IfcFaceBoundBuilder());
		this.put("IFCRELREFERENCEDINSPATIALSTRUCTURE", new IfcRelReferencedInSpatialStructureBuilder());
		this.put("IFCSYSTEMFURNITUREELEMENTTYPE", new IfcSystemFurnitureElementTypeBuilder());
		this.put("IFCANNOTATION", new IfcAnnotationBuilder());
		this.put("IFCTRAPEZIUMPROFILEDEF", new IfcTrapeziumProfileDefBuilder());
		this.put("IFCFILLAREASTYLE", new IfcFillAreaStyleBuilder());
		this.put("IFCCONNECTIONPOINTGEOMETRY", new IfcConnectionPointGeometryBuilder());
		this.put("IFCRELASSOCIATESDOCUMENT", new IfcRelAssociatesDocumentBuilder());
		this.put("IFCPRESENTATIONLAYERWITHSTYLE", new IfcPresentationLayerWithStyleBuilder());
		this.put("IFCFLOWCONTROLLER", new IfcFlowControllerBuilder());
		this.put("IFCROOF", new IfcRoofBuilder());
		this.put("IFCVECTOR", new IfcVectorBuilder());
		this.put("IFCGENERALPROFILEPROPERTIES", new IfcGeneralProfilePropertiesBuilder());
		this.put("IFCHUMIDIFIERTYPE", new IfcHumidifierTypeBuilder());
		this.put("IFCWINDOWLININGPROPERTIES", new IfcWindowLiningPropertiesBuilder());
		this.put("IFCDISCRETEACCESSORY", new IfcDiscreteAccessoryBuilder());
		this.put("IFCBUILDINGELEMENTPROXYTYPE", new IfcBuildingElementProxyTypeBuilder());
		this.put("IFCHALFSPACESOLID", new IfcHalfSpaceSolidBuilder());
		this.put("IFCFURNISHINGELEMENT", new IfcFurnishingElementBuilder());
		this.put("IFCAIRTERMINALTYPE", new IfcAirTerminalTypeBuilder());
		this.put("IFCAPPROVALPROPERTYRELATIONSHIP", new IfcApprovalPropertyRelationshipBuilder());
		this.put("IFCMECHANICALCONCRETEMATERIALPROPERTIES", new IfcMechanicalConcreteMaterialPropertiesBuilder());
		this.put("IFCANGULARDIMENSION", new IfcAngularDimensionBuilder());
		this.put("IFCPUMPTYPE", new IfcPumpTypeBuilder());
		this.put("IFCLOOP", new IfcLoopBuilder());
		this.put("IFCFURNITURETYPE", new IfcFurnitureTypeBuilder());
		this.put("IFCPLANARBOX", new IfcPlanarBoxBuilder());
		this.put("IFCPOSTALADDRESS", new IfcPostalAddressBuilder());
		this.put("IFCOBJECTIVE", new IfcObjectiveBuilder());
		this.put("IFCREVOLVEDAREASOLID", new IfcRevolvedAreaSolidBuilder());
		this.put("IFCRELASSIGNSTOPROJECTORDER", new IfcRelAssignsToProjectOrderBuilder());
		this.put("IFCFLOWTERMINAL", new IfcFlowTerminalBuilder());
		this.put("IFCPROJECT", new IfcProjectBuilder());
		this.put("IFCWALL", new IfcWallBuilder());
		this.put("IFCRELPROJECTSELEMENT", new IfcRelProjectsElementBuilder());
		this.put("IFCDOCUMENTREFERENCE", new IfcDocumentReferenceBuilder());
		this.put("IFCRELASSOCIATESLIBRARY", new IfcRelAssociatesLibraryBuilder());
		this.put("IFCCOMPRESSORTYPE", new IfcCompressorTypeBuilder());
		this.put("IFCSPACEPROGRAM", new IfcSpaceProgramBuilder());
		this.put("IFCBUILDINGSTOREY", new IfcBuildingStoreyBuilder());
		this.put("IFCSWEPTDISKSOLID", new IfcSweptDiskSolidBuilder());
		this.put("IFCTIMESERIESREFERENCERELATIONSHIP", new IfcTimeSeriesReferenceRelationshipBuilder());
		this.put("IFC2DCOMPOSITECURVE", new Ifc2DCompositeCurveBuilder());
		this.put("IFCCARTESIANTRANSFORMATIONOPERATOR2DNONUNIFORM", new IfcCartesianTransformationOperator2DnonUniformBuilder());
		this.put("IFCCHAMFEREDGEFEATURE", new IfcChamferEdgeFeatureBuilder());
		this.put("IFCPROJECTIONELEMENT", new IfcProjectionElementBuilder());
		this.put("IFCMECHANICALFASTENER", new IfcMechanicalFastenerBuilder());
		this.put("IFCTRANSPORTELEMENT", new IfcTransportElementBuilder());
		this.put("IFCTIMESERIESSCHEDULE", new IfcTimeSeriesScheduleBuilder());
		this.put("IFCRELSEQUENCE", new IfcRelSequenceBuilder());
		this.put("IFCFILLAREASTYLETILESYMBOLWITHSTYLE", new IfcFillAreaStyleTileSymbolWithStyleBuilder());
		this.put("IFCFLUIDFLOWPROPERTIES", new IfcFluidFlowPropertiesBuilder());
		this.put("IFCFOOTING", new IfcFootingBuilder());
		this.put("IFCPRODUCTREPRESENTATION", new IfcProductRepresentationBuilder());
		this.put("IFCCONNECTEDFACESET", new IfcConnectedFaceSetBuilder());
		this.put("IFCBUILDINGELEMENTPART", new IfcBuildingElementPartBuilder());
		this.put("IFCAXIS2PLACEMENT3D", new IfcAxis2Placement3DBuilder());
		this.put("IFCLIGHTSOURCESPOT", new IfcLightSourceSpotBuilder());
		this.put("IFCSTRUCTURALPLANARACTION", new IfcStructuralPlanarActionBuilder());
		this.put("IFCPROJECTIONCURVE", new IfcProjectionCurveBuilder());
		this.put("IFCLINEARDIMENSION", new IfcLinearDimensionBuilder());
		this.put("IFCCLASSIFICATION", new IfcClassificationBuilder());
		this.put("IFCFLOWMOVINGDEVICE", new IfcFlowMovingDeviceBuilder());
		this.put("IFCUSHAPEPROFILEDEF", new IfcUShapeProfileDefBuilder());
		this.put("IFCPERSONANDORGANIZATION", new IfcPersonAndOrganizationBuilder());
		this.put("IFCMETRIC", new IfcMetricBuilder());
		this.put("IFCLIGHTFIXTURETYPE", new IfcLightFixtureTypeBuilder());
		this.put("IFCSURFACEOFREVOLUTION", new IfcSurfaceOfRevolutionBuilder());
		this.put("IFCCABLECARRIERFITTINGTYPE", new IfcCableCarrierFittingTypeBuilder());
		this.put("IFCSTRUCTURALLOADTEMPERATURE", new IfcStructuralLoadTemperatureBuilder());
		this.put("IFCRIGHTCIRCULARCYLINDER", new IfcRightCircularCylinderBuilder());
		this.put("IFCCOSTVALUE", new IfcCostValueBuilder());
		this.put("IFCELECTRICALCIRCUIT", new IfcElectricalCircuitBuilder());
		this.put("IFCFILLAREASTYLEHATCHING", new IfcFillAreaStyleHatchingBuilder());
		this.put("IFCSHAPEASPECT", new IfcShapeAspectBuilder());
		this.put("IFCRELCONNECTSPATHELEMENTS", new IfcRelConnectsPathElementsBuilder());
		this.put("IFCCONSTRUCTIONMATERIALRESOURCE", new IfcConstructionMaterialResourceBuilder());
		this.put("IFCIRREGULARTIMESERIES", new IfcIrregularTimeSeriesBuilder());
		this.put("IFCSTRUCTURALLINEARACTION", new IfcStructuralLinearActionBuilder());
		this.put("IFCGENERALMATERIALPROPERTIES", new IfcGeneralMaterialPropertiesBuilder());
		this.put("IFCCOMPOSITECURVESEGMENT", new IfcCompositeCurveSegmentBuilder());
		this.put("IFCGRIDPLACEMENT", new IfcGridPlacementBuilder());
		this.put("IFCOPTICALMATERIALPROPERTIES", new IfcOpticalMaterialPropertiesBuilder());
		this.put("IFCDATEANDTIME", new IfcDateAndTimeBuilder());
		this.put("IFCSTRUCTURALLINEARACTIONVARYING", new IfcStructuralLinearActionVaryingBuilder());
		this.put("IFCEXTERNALLYDEFINEDSYMBOL", new IfcExternallyDefinedSymbolBuilder());
		this.put("IFCDISTRIBUTIONCONTROLELEMENT", new IfcDistributionControlElementBuilder());
		this.put("IFCINVENTORY", new IfcInventoryBuilder());
		this.put("IFCTSHAPEPROFILEDEF", new IfcTShapeProfileDefBuilder());
		this.put("IFCDOORPANELPROPERTIES", new IfcDoorPanelPropertiesBuilder());
		this.put("IFCDIMENSIONCALLOUTRELATIONSHIP", new IfcDimensionCalloutRelationshipBuilder());
		this.put("IFCGRID", new IfcGridBuilder());
		this.put("IFCDISTRIBUTIONCHAMBERELEMENT", new IfcDistributionChamberElementBuilder());
		this.put("IFCFLOWINSTRUMENTTYPE", new IfcFlowInstrumentTypeBuilder());
		this.put("IFCTOPOLOGYREPRESENTATION", new IfcTopologyRepresentationBuilder());
		this.put("IFCDISTRIBUTIONPORT", new IfcDistributionPortBuilder());
		this.put("IFCANNOTATIONSURFACEOCCURRENCE", new IfcAnnotationSurfaceOccurrenceBuilder());
		this.put("IFCCURVESTYLE", new IfcCurveStyleBuilder());
		this.put("IFCRELFLOWCONTROLELEMENTS", new IfcRelFlowControlElementsBuilder());
		this.put("IFCCARTESIANTRANSFORMATIONOPERATOR3D", new IfcCartesianTransformationOperator3DBuilder());
		this.put("IFCSTRUCTURALSURFACEMEMBERVARYING", new IfcStructuralSurfaceMemberVaryingBuilder());
		this.put("IFCFASTENER", new IfcFastenerBuilder());
		this.put("IFCSTRUCTURALANALYSISMODEL", new IfcStructuralAnalysisModelBuilder());
		this.put("IFCLIBRARYREFERENCE", new IfcLibraryReferenceBuilder());
		this.put("IFCRELAXATION", new IfcRelaxationBuilder());
		this.put("IFCSOUNDPROPERTIES", new IfcSoundPropertiesBuilder());
		this.put("IFCCLOSEDSHELL", new IfcClosedShellBuilder());
		this.put("IFCRELFILLSELEMENT", new IfcRelFillsElementBuilder());
		this.put("IFCCURTAINWALLTYPE", new IfcCurtainWallTypeBuilder());
		this.put("IFCAXIS2PLACEMENT2D", new IfcAxis2Placement2DBuilder());
		this.put("IFCFLOWMETERTYPE", new IfcFlowMeterTypeBuilder());
		this.put("IFCWORKSCHEDULE", new IfcWorkScheduleBuilder());
		this.put("IFCCONNECTIONPORTGEOMETRY", new IfcConnectionPortGeometryBuilder());
		this.put("IFCCREWRESOURCE", new IfcCrewResourceBuilder());
		this.put("IFCSLABTYPE", new IfcSlabTypeBuilder());
		this.put("IFCRELCONNECTSSTRUCTURALACTIVITY", new IfcRelConnectsStructuralActivityBuilder());
		this.put("IFCCOOLINGTOWERTYPE", new IfcCoolingTowerTypeBuilder());
		this.put("IFCOUTLETTYPE", new IfcOutletTypeBuilder());
		this.put("IFCRELCOVERSSPACES", new IfcRelCoversSpacesBuilder());
		this.put("IFCRELCONNECTSWITHREALIZINGELEMENTS", new IfcRelConnectsWithRealizingElementsBuilder());
		this.put("IFCDRAUGHTINGCALLOUTRELATIONSHIP", new IfcDraughtingCalloutRelationshipBuilder());
		this.put("IFCSECTIONREINFORCEMENTPROPERTIES", new IfcSectionReinforcementPropertiesBuilder());
		this.put("IFCELECTRICALELEMENT", new IfcElectricalElementBuilder());
		this.put("IFCSYSTEM", new IfcSystemBuilder());
		this.put("IFCRELOVERRIDESPROPERTIES", new IfcRelOverridesPropertiesBuilder());
		this.put("IFCWINDOWSTYLE", new IfcWindowStyleBuilder());
		this.put("IFCSYMBOLSTYLE", new IfcSymbolStyleBuilder());
		this.put("IFCSENSORTYPE", new IfcSensorTypeBuilder());
		this.put("IFCSTRUCTURALLOADGROUP", new IfcStructuralLoadGroupBuilder());
		this.put("IFCMECHANICALFASTENERTYPE", new IfcMechanicalFastenerTypeBuilder());
		this.put("IFCCONNECTIONSURFACEGEOMETRY", new IfcConnectionSurfaceGeometryBuilder());
		this.put("IFCRELNESTS", new IfcRelNestsBuilder());
		this.put("IFCMATERIALLIST", new IfcMaterialListBuilder());
		this.put("IFCDISTRIBUTIONFLOWELEMENT", new IfcDistributionFlowElementBuilder());
		this.put("IFCTABLE", new IfcTableBuilder());
		this.put("IFCROUNDEDRECTANGLEPROFILEDEF", new IfcRoundedRectangleProfileDefBuilder());
		this.put("IFCANNOTATIONSURFACE", new IfcAnnotationSurfaceBuilder());
		this.put("IFCEXTENDEDMATERIALPROPERTIES", new IfcExtendedMaterialPropertiesBuilder());
		this.put("IFCCLASSIFICATIONITEMRELATIONSHIP", new IfcClassificationItemRelationshipBuilder());
		this.put("IFCPROPERTYBOUNDEDVALUE", new IfcPropertyBoundedValueBuilder());
		this.put("IFCCOMPOSITEPROFILEDEF", new IfcCompositeProfileDefBuilder());
		this.put("IFCGEOMETRICREPRESENTATIONCONTEXT", new IfcGeometricRepresentationContextBuilder());
		this.put("IFCASYMMETRICISHAPEPROFILEDEF", new IfcAsymmetricIShapeProfileDefBuilder());
		this.put("IFCRELCONNECTSPORTS", new IfcRelConnectsPortsBuilder());
		this.put("IFCQUANTITYLENGTH", new IfcQuantityLengthBuilder());
		this.put("IFCSLIPPAGECONNECTIONCONDITION", new IfcSlippageConnectionConditionBuilder());
		this.put("IFCREPRESENTATION", new IfcRepresentationBuilder());
		this.put("IFCRELOCCUPIESSPACES", new IfcRelOccupiesSpacesBuilder());
		this.put("IFCZSHAPEPROFILEDEF", new IfcZShapeProfileDefBuilder());
		this.put("IFCDIMENSIONCURVEDIRECTEDCALLOUT", new IfcDimensionCurveDirectedCalloutBuilder());
		this.put("IFCSURFACECURVESWEPTAREASOLID", new IfcSurfaceCurveSweptAreaSolidBuilder());
		this.put("IFCPROPERTYSINGLEVALUE", new IfcPropertySingleValueBuilder());
		this.put("IFCCOILTYPE", new IfcCoilTypeBuilder());
		this.put("IFCBLOCK", new IfcBlockBuilder());
		this.put("IFCCARTESIANTRANSFORMATIONOPERATOR2D", new IfcCartesianTransformationOperator2DBuilder());
		this.put("IFCPHYSICALCOMPLEXQUANTITY", new IfcPhysicalComplexQuantityBuilder());
		this.put("INTEGER", new INTEGERBuilder());
		this.put("DOUBLE", new DOUBLEBuilder());
		this.put("BOOLEAN", new BOOLEANBuilder());
		this.put("BINARY", new BINARYBuilder());
		this.put("REAL", new REALBuilder());
		this.put("STRING", new STRINGBuilder());
		this.put("LOGICAL", new LOGICALBuilder());
	}


static class IfcSequenceEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcSequenceEnum();}}

static class IfcWorkControlTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcWorkControlTypeEnum();}}

static class IfcIntegerCountRateMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcIntegerCountRateMeasure();}}

static class IfcProfileTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcProfileTypeEnum();}}

static class IfcModulusOfSubgradeReactionMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcModulusOfSubgradeReactionMeasure();}}

static class IfcRatioMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcRatioMeasure();}}

static class IfcArithmeticOperatorEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcArithmeticOperatorEnum();}}

static class IfcServiceLifeFactorTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcServiceLifeFactorTypeEnum();}}

static class IfcTrimmingPreferenceBuilder implements ClassBuilder
{public Object instance(){return new IfcTrimmingPreference();}}

static class IfcEnvironmentalImpactCategoryEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcEnvironmentalImpactCategoryEnum();}}

static class IfcPropertySourceEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcPropertySourceEnum();}}

static class IfcDocumentStatusEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcDocumentStatusEnum();}}

static class IfcWindowStyleConstructionEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcWindowStyleConstructionEnum();}}

static class IfcStateEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcStateEnum();}}

static class IfcPlateTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcPlateTypeEnum();}}

static class IfcCondenserTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcCondenserTypeEnum();}}

static class IfcPhysicalOrVirtualEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcPhysicalOrVirtualEnum();}}

static class IfcElectricChargeMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcElectricChargeMeasure();}}

static class IfcMassFlowRateMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcMassFlowRateMeasure();}}

static class IfcPositiveLengthMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcPositiveLengthMeasure();}}

static class IfcVolumetricFlowRateMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcVolumetricFlowRateMeasure();}}

static class IfcCompoundPlaneAngleMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcCompoundPlaneAngleMeasure();}}

static class IfcYearNumberBuilder implements ClassBuilder
{public Object instance(){return new IfcYearNumber();}}

static class IfcElementCompositionEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcElementCompositionEnum();}}

static class IfcJunctionBoxTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcJunctionBoxTypeEnum();}}

static class IfcCoolingTowerTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcCoolingTowerTypeEnum();}}

static class IfcTimeStampBuilder implements ClassBuilder
{public Object instance(){return new IfcTimeStamp();}}

static class IfcComplexNumberBuilder implements ClassBuilder
{public Object instance(){return new IfcComplexNumber();}}

static class IfcSanitaryTerminalTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcSanitaryTerminalTypeEnum();}}

static class IfcThermalResistanceMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcThermalResistanceMeasure();}}

static class IfcTubeBundleTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcTubeBundleTypeEnum();}}

static class IfcNormalisedRatioMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcNormalisedRatioMeasure();}}

static class IfcActionSourceTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcActionSourceTypeEnum();}}

static class IfcDaylightSavingHourBuilder implements ClassBuilder
{public Object instance(){return new IfcDaylightSavingHour();}}

static class SchemaNameBuilder implements ClassBuilder
{public Object instance(){return new SchemaName();}}

static class IfcLuminousIntensityMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcLuminousIntensityMeasure();}}

static class IfcElectricTimeControlTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcElectricTimeControlTypeEnum();}}

static class IfcPumpTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcPumpTypeEnum();}}

static class IfcRotationalStiffnessMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcRotationalStiffnessMeasure();}}

static class IfcLinearForceMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcLinearForceMeasure();}}

static class IfcProjectOrderTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcProjectOrderTypeEnum();}}

static class IfcTimeMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcTimeMeasure();}}

static class IfcTextFontNameBuilder implements ClassBuilder
{public Object instance(){return new IfcTextFontName();}}

static class IfcDimensionExtentUsageBuilder implements ClassBuilder
{public Object instance(){return new IfcDimensionExtentUsage();}}

static class TimeStampTextBuilder implements ClassBuilder
{public Object instance(){return new TimeStampText();}}

static class IfcObjectiveEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcObjectiveEnum();}}

static class IfcSpecularRoughnessBuilder implements ClassBuilder
{public Object instance(){return new IfcSpecularRoughness();}}

static class IfcElectricCapacitanceMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcElectricCapacitanceMeasure();}}

static class IfcRadioActivityMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcRadioActivityMeasure();}}

static class IfcMagneticFluxMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcMagneticFluxMeasure();}}

static class IfcMemberTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcMemberTypeEnum();}}

static class IfcSolidAngleMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcSolidAngleMeasure();}}

static class IfcSIPrefixBuilder implements ClassBuilder
{public Object instance(){return new IfcSIPrefix();}}

static class IfcGlobalOrLocalEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcGlobalOrLocalEnum();}}

static class IfcActuatorTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcActuatorTypeEnum();}}

static class IfcRotationalFrequencyMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcRotationalFrequencyMeasure();}}

static class IfcElementAssemblyTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcElementAssemblyTypeEnum();}}

static class IfcDayInMonthNumberBuilder implements ClassBuilder
{public Object instance(){return new IfcDayInMonthNumber();}}

static class IfcCurrencyEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcCurrencyEnum();}}

static class IfcPressureMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcPressureMeasure();}}

static class IfcEnergySequenceEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcEnergySequenceEnum();}}

static class IfcGasTerminalTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcGasTerminalTypeEnum();}}

static class IfcWallTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcWallTypeEnum();}}

static class IfcBSplineCurveFormBuilder implements ClassBuilder
{public Object instance(){return new IfcBSplineCurveForm();}}

static class IfcServiceLifeTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcServiceLifeTypeEnum();}}

static class IfcMinuteInHourBuilder implements ClassBuilder
{public Object instance(){return new IfcMinuteInHour();}}

static class IfcProjectOrderRecordTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcProjectOrderRecordTypeEnum();}}

static class IfcTextBuilder implements ClassBuilder
{public Object instance(){return new IfcText();}}

static class IfcSurfaceSideBuilder implements ClassBuilder
{public Object instance(){return new IfcSurfaceSide();}}

static class IfcControllerTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcControllerTypeEnum();}}

static class IfcForceMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcForceMeasure();}}

static class IfcFlowMeterTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcFlowMeterTypeEnum();}}

static class IfcSoundPressureMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcSoundPressureMeasure();}}

static class IfcFontVariantBuilder implements ClassBuilder
{public Object instance(){return new IfcFontVariant();}}

static class IfcAccelerationMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcAccelerationMeasure();}}

static class IfcElectricDistributionPointFunctionEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcElectricDistributionPointFunctionEnum();}}

static class IfcFrequencyMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcFrequencyMeasure();}}

static class IfcBenchmarkEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcBenchmarkEnum();}}

static class IfcChangeActionEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcChangeActionEnum();}}

static class IfcCableSegmentTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcCableSegmentTypeEnum();}}

static class IfcMassDensityMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcMassDensityMeasure();}}

static class IfcLuminousFluxMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcLuminousFluxMeasure();}}

static class IfcLabelBuilder implements ClassBuilder
{public Object instance(){return new IfcLabel();}}

static class IfcDuctSilencerTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcDuctSilencerTypeEnum();}}

static class IfcDoorStyleConstructionEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcDoorStyleConstructionEnum();}}

static class IfcLampTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcLampTypeEnum();}}

static class IfcFontWeightBuilder implements ClassBuilder
{public Object instance(){return new IfcFontWeight();}}

static class IfcRoleEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcRoleEnum();}}

static class IfcReflectanceMethodEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcReflectanceMethodEnum();}}

static class IfcWindowPanelOperationEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcWindowPanelOperationEnum();}}

static class IfcSurfaceTextureEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcSurfaceTextureEnum();}}

static class IfcElectricCurrentMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcElectricCurrentMeasure();}}

static class IfcAssemblyPlaceEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcAssemblyPlaceEnum();}}

static class IfcEvaporativeCoolerTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcEvaporativeCoolerTypeEnum();}}

static class IfcSpecificHeatCapacityMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcSpecificHeatCapacityMeasure();}}

static class IfcDataOriginEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcDataOriginEnum();}}

static class IfcFilterTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcFilterTypeEnum();}}

static class IfcPositiveRatioMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcPositiveRatioMeasure();}}

static class IfcTextTransformationBuilder implements ClassBuilder
{public Object instance(){return new IfcTextTransformation();}}

static class IfcDirectionSenseEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcDirectionSenseEnum();}}

static class IfcTextPathBuilder implements ClassBuilder
{public Object instance(){return new IfcTextPath();}}

static class IfcMonetaryMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcMonetaryMeasure();}}

static class IfcElectricFlowStorageDeviceTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcElectricFlowStorageDeviceTypeEnum();}}

static class IfcTorqueMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcTorqueMeasure();}}

static class IfcWindowStyleOperationEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcWindowStyleOperationEnum();}}

static class IfcIlluminanceMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcIlluminanceMeasure();}}

static class IfcTransformerTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcTransformerTypeEnum();}}

static class IfcAnalysisTheoryTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcAnalysisTheoryTypeEnum();}}

static class IfcFlowInstrumentTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcFlowInstrumentTypeEnum();}}

static class IfcHumidifierTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcHumidifierTypeEnum();}}

static class IfcMagneticFluxDensityMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcMagneticFluxDensityMeasure();}}

static class IfcCurvatureMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcCurvatureMeasure();}}

static class IfcKinematicViscosityMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcKinematicViscosityMeasure();}}

static class IfcPHMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcPHMeasure();}}

static class IfcHeatFluxDensityMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcHeatFluxDensityMeasure();}}

static class IfcConnectionTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcConnectionTypeEnum();}}

static class IfcRampFlightTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcRampFlightTypeEnum();}}

static class IfcSpecularExponentBuilder implements ClassBuilder
{public Object instance(){return new IfcSpecularExponent();}}

static class IfcMassMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcMassMeasure();}}

static class IfcThermalLoadSourceEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcThermalLoadSourceEnum();}}

static class IfcEvaporatorTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcEvaporatorTypeEnum();}}

static class IfcSectionTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcSectionTypeEnum();}}

static class IfcDoorPanelPositionEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcDoorPanelPositionEnum();}}

static class IfcSIUnitNameBuilder implements ClassBuilder
{public Object instance(){return new IfcSIUnitName();}}

static class IfcWasteTerminalTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcWasteTerminalTypeEnum();}}

static class IfcUnitEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcUnitEnum();}}

static class IfcTimeSeriesScheduleTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcTimeSeriesScheduleTypeEnum();}}

static class IfcLightEmissionSourceEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcLightEmissionSourceEnum();}}

static class IfcElectricResistanceMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcElectricResistanceMeasure();}}

static class IfcThermalConductivityMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcThermalConductivityMeasure();}}

static class IfcElectricMotorTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcElectricMotorTypeEnum();}}

static class IfcLightFixtureTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcLightFixtureTypeEnum();}}

static class IfcBuildingElementProxyTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcBuildingElementProxyTypeEnum();}}

static class IfcPileTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcPileTypeEnum();}}

static class IfcThermodynamicTemperatureMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcThermodynamicTemperatureMeasure();}}

static class IfcDoorPanelOperationEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcDoorPanelOperationEnum();}}

static class IfcFontStyleBuilder implements ClassBuilder
{public Object instance(){return new IfcFontStyle();}}

static class IfcLengthMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcLengthMeasure();}}

static class IfcMomentOfInertiaMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcMomentOfInertiaMeasure();}}

static class IfcRealBuilder implements ClassBuilder
{public Object instance(){return new IfcReal();}}

static class IfcCurtainWallTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcCurtainWallTypeEnum();}}

static class IfcWarpingMomentMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcWarpingMomentMeasure();}}

static class IfcCountMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcCountMeasure();}}

static class IfcIonConcentrationMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcIonConcentrationMeasure();}}

static class IfcPlaneAngleMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcPlaneAngleMeasure();}}

static class IfcElectricConductanceMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcElectricConductanceMeasure();}}

static class IfcFireSuppressionTerminalTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcFireSuppressionTerminalTypeEnum();}}

static class IfcMonthInYearNumberBuilder implements ClassBuilder
{public Object instance(){return new IfcMonthInYearNumber();}}

static class IfcDynamicViscosityMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcDynamicViscosityMeasure();}}

static class IfcSectionModulusMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcSectionModulusMeasure();}}

static class IfcHourInDayBuilder implements ClassBuilder
{public Object instance(){return new IfcHourInDay();}}

static class IfcRotationalMassMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcRotationalMassMeasure();}}

static class IfcBooleanOperatorBuilder implements ClassBuilder
{public Object instance(){return new IfcBooleanOperator();}}

static class IfcElectricHeaterTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcElectricHeaterTypeEnum();}}

static class IfcObjectTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcObjectTypeEnum();}}

static class IfcInternalOrExternalEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcInternalOrExternalEnum();}}

static class IfcAngularVelocityMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcAngularVelocityMeasure();}}

static class IfcFanTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcFanTypeEnum();}}

static class IfcLogicalOperatorEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcLogicalOperatorEnum();}}

static class IfcAddressTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcAddressTypeEnum();}}

static class IfcDistributionChamberElementTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcDistributionChamberElementTypeEnum();}}

static class IfcSoundScaleEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcSoundScaleEnum();}}

static class IfcGeometricProjectionEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcGeometricProjectionEnum();}}

static class IfcStairTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcStairTypeEnum();}}

static class IfcDuctFittingTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcDuctFittingTypeEnum();}}

static class IfcLinearVelocityMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcLinearVelocityMeasure();}}

static class IfcCooledBeamTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcCooledBeamTypeEnum();}}

static class IfcDescriptiveMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcDescriptiveMeasure();}}

static class IfcElectricApplianceTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcElectricApplianceTypeEnum();}}

static class IfcSlabTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcSlabTypeEnum();}}

static class IfcVolumeMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcVolumeMeasure();}}

static class IfcProjectedOrTrueLengthEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcProjectedOrTrueLengthEnum();}}

static class IfcAreaMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcAreaMeasure();}}

static class IfcIntegerBuilder implements ClassBuilder
{public Object instance(){return new IfcInteger();}}

static class IfcMassPerLengthMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcMassPerLengthMeasure();}}

static class IfcDuctSegmentTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcDuctSegmentTypeEnum();}}

static class IfcElectricCurrentEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcElectricCurrentEnum();}}

static class IfcIdentifierBuilder implements ClassBuilder
{public Object instance(){return new IfcIdentifier();}}

static class IfcLinearStiffnessMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcLinearStiffnessMeasure();}}

static class IfcTimeSeriesDataTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcTimeSeriesDataTypeEnum();}}

static class IfcLightDistributionCurveEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcLightDistributionCurveEnum();}}

static class IfcLoadGroupTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcLoadGroupTypeEnum();}}

static class IfcSpaceTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcSpaceTypeEnum();}}

static class IfcSoundPowerMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcSoundPowerMeasure();}}

static class IfcShearModulusMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcShearModulusMeasure();}}

static class IfcFootingTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcFootingTypeEnum();}}

static class IfcMoistureDiffusivityMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcMoistureDiffusivityMeasure();}}

static class IfcAirTerminalBoxTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcAirTerminalBoxTypeEnum();}}

static class IfcStructuralSurfaceTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuralSurfaceTypeEnum();}}

static class IfcStructuralCurveTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuralCurveTypeEnum();}}

static class IfcLuminousIntensityDistributionMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcLuminousIntensityDistributionMeasure();}}

static class IfcDoorStyleOperationEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcDoorStyleOperationEnum();}}

static class IfcModulusOfLinearSubgradeReactionMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcModulusOfLinearSubgradeReactionMeasure();}}

static class IfcSpaceHeaterTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcSpaceHeaterTypeEnum();}}

static class IfcPipeFittingTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcPipeFittingTypeEnum();}}

static class IfcBooleanBuilder implements ClassBuilder
{public Object instance(){return new IfcBoolean();}}

static class IfcThermalLoadTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcThermalLoadTypeEnum();}}

static class IfcSwitchingDeviceTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcSwitchingDeviceTypeEnum();}}

static class IfcProcedureTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcProcedureTypeEnum();}}

static class IfcTankTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcTankTypeEnum();}}

static class IfcSectionalAreaIntegralMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcSectionalAreaIntegralMeasure();}}

static class IfcBeamTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcBeamTypeEnum();}}

static class IfcMotorConnectionTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcMotorConnectionTypeEnum();}}

static class IfcDamperTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcDamperTypeEnum();}}

static class IfcUnitaryEquipmentTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcUnitaryEquipmentTypeEnum();}}

static class IfcReinforcingBarSurfaceEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcReinforcingBarSurfaceEnum();}}

static class IfcCableCarrierSegmentTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcCableCarrierSegmentTypeEnum();}}

static class IfcPileConstructionEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcPileConstructionEnum();}}

static class IfcDimensionCountBuilder implements ClassBuilder
{public Object instance(){return new IfcDimensionCount();}}

static class IfcCableCarrierFittingTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcCableCarrierFittingTypeEnum();}}

static class IfcPermeableCoveringOperationEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcPermeableCoveringOperationEnum();}}

static class IfcDerivedUnitEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcDerivedUnitEnum();}}

static class IfcMolecularWeightMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcMolecularWeightMeasure();}}

static class IfcEnergyMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcEnergyMeasure();}}

static class IfcPipeSegmentTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcPipeSegmentTypeEnum();}}

static class IfcGloballyUniqueIdBuilder implements ClassBuilder
{public Object instance(){return new IfcGloballyUniqueId();}}

static class IfcTextAlignmentBuilder implements ClassBuilder
{public Object instance(){return new IfcTextAlignment();}}

static class IfcThermalAdmittanceMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcThermalAdmittanceMeasure();}}

static class IfcIsothermalMoistureCapacityMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcIsothermalMoistureCapacityMeasure();}}

static class IfcAirTerminalTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcAirTerminalTypeEnum();}}

static class IfcHeatExchangerTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcHeatExchangerTypeEnum();}}

static class IfcInductanceMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcInductanceMeasure();}}

static class IfcNumericMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcNumericMeasure();}}

static class IfcConstraintEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcConstraintEnum();}}

static class IfcFlowDirectionEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcFlowDirectionEnum();}}

static class IfcAmountOfSubstanceMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcAmountOfSubstanceMeasure();}}

static class IfcPowerMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcPowerMeasure();}}

static class IfcCoilTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcCoilTypeEnum();}}

static class IfcProtectiveDeviceTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcProtectiveDeviceTypeEnum();}}

static class IfcNullStyleBuilder implements ClassBuilder
{public Object instance(){return new IfcNullStyle();}}

static class IfcAnalysisModelTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcAnalysisModelTypeEnum();}}

static class IfcSensorTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcSensorTypeEnum();}}

static class IfcAlarmTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcAlarmTypeEnum();}}

static class IfcOutletTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcOutletTypeEnum();}}

static class IfcResourceConsumptionEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcResourceConsumptionEnum();}}

static class IfcDoseEquivalentMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcDoseEquivalentMeasure();}}

static class IfcHeatingValueMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcHeatingValueMeasure();}}

static class IfcInventoryTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcInventoryTypeEnum();}}

static class IfcCoveringTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcCoveringTypeEnum();}}

static class IfcRailingTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcRailingTypeEnum();}}

static class IfcReinforcingBarRoleEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcReinforcingBarRoleEnum();}}

static class IfcWarpingConstantMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcWarpingConstantMeasure();}}

static class IfcValveTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcValveTypeEnum();}}

static class IfcPresentableTextBuilder implements ClassBuilder
{public Object instance(){return new IfcPresentableText();}}

static class IfcTransportElementTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcTransportElementTypeEnum();}}

static class IfcBoxAlignmentBuilder implements ClassBuilder
{public Object instance(){return new IfcBoxAlignment();}}

static class IfcStackTerminalTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcStackTerminalTypeEnum();}}

static class IfcCompressorTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcCompressorTypeEnum();}}

static class IfcRampTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcRampTypeEnum();}}

static class IfcRoofTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcRoofTypeEnum();}}

static class IfcLinearMomentMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcLinearMomentMeasure();}}

static class IfcPositivePlaneAngleMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcPositivePlaneAngleMeasure();}}

static class IfcVibrationIsolatorTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcVibrationIsolatorTypeEnum();}}

static class IfcAheadOrBehindBuilder implements ClassBuilder
{public Object instance(){return new IfcAheadOrBehind();}}

static class IfcSecondInMinuteBuilder implements ClassBuilder
{public Object instance(){return new IfcSecondInMinute();}}

static class IfcParameterValueBuilder implements ClassBuilder
{public Object instance(){return new IfcParameterValue();}}

static class IfcElectricVoltageMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcElectricVoltageMeasure();}}

static class IfcWindowPanelPositionEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcWindowPanelPositionEnum();}}

static class IfcPlanarForceMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcPlanarForceMeasure();}}

static class IfcThermalExpansionCoefficientMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcThermalExpansionCoefficientMeasure();}}

static class IfcLogicalBuilder implements ClassBuilder
{public Object instance(){return new IfcLogical();}}

static class IfcAbsorbedDoseMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcAbsorbedDoseMeasure();}}

static class IfcColumnTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcColumnTypeEnum();}}

static class IfcCostScheduleTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcCostScheduleTypeEnum();}}

static class IfcModulusOfElasticityMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcModulusOfElasticityMeasure();}}

static class IfcRibPlateDirectionEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcRibPlateDirectionEnum();}}

static class IfcAirToAirHeatRecoveryTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcAirToAirHeatRecoveryTypeEnum();}}

static class IfcThermalTransmittanceMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcThermalTransmittanceMeasure();}}

static class IfcBoilerTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcBoilerTypeEnum();}}

static class IfcTemperatureGradientMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcTemperatureGradientMeasure();}}

static class IfcDocumentConfidentialityEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcDocumentConfidentialityEnum();}}

static class IfcElectricGeneratorTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcElectricGeneratorTypeEnum();}}

static class IfcModulusOfRotationalSubgradeReactionMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcModulusOfRotationalSubgradeReactionMeasure();}}

static class IfcOccupantTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcOccupantTypeEnum();}}

static class IfcContextDependentMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcContextDependentMeasure();}}

static class IfcTextDecorationBuilder implements ClassBuilder
{public Object instance(){return new IfcTextDecoration();}}

static class IfcTransitionCodeBuilder implements ClassBuilder
{public Object instance(){return new IfcTransitionCode();}}

static class IfcStairFlightTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcStairFlightTypeEnum();}}

static class IfcActionTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcActionTypeEnum();}}

static class IfcVaporPermeabilityMeasureBuilder implements ClassBuilder
{public Object instance(){return new IfcVaporPermeabilityMeasure();}}

static class IfcLayerSetDirectionEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcLayerSetDirectionEnum();}}

static class IfcTendonTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcTendonTypeEnum();}}

static class IfcChillerTypeEnumBuilder implements ClassBuilder
{public Object instance(){return new IfcChillerTypeEnum();}}

static class IfcSIUnitBuilder implements ClassBuilder
{public Object instance(){return new IfcSIUnit();}}

static class IfcRelAssignsToActorBuilder implements ClassBuilder
{public Object instance(){return new IfcRelAssignsToActor();}}

static class IfcDraughtingPreDefinedTextFontBuilder implements ClassBuilder
{public Object instance(){return new IfcDraughtingPreDefinedTextFont();}}

static class IfcDirectionBuilder implements ClassBuilder
{public Object instance(){return new IfcDirection();}}

static class IfcJunctionBoxTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcJunctionBoxType();}}

static class IfcFireSuppressionTerminalTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcFireSuppressionTerminalType();}}

static class IfcRightCircularConeBuilder implements ClassBuilder
{public Object instance(){return new IfcRightCircularCone();}}

static class IfcReinforcingBarBuilder implements ClassBuilder
{public Object instance(){return new IfcReinforcingBar();}}

static class IfcCoordinatedUniversalTimeOffsetBuilder implements ClassBuilder
{public Object instance(){return new IfcCoordinatedUniversalTimeOffset();}}

static class IfcFlowStorageDeviceBuilder implements ClassBuilder
{public Object instance(){return new IfcFlowStorageDevice();}}

static class IfcDuctSegmentTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcDuctSegmentType();}}

static class IfcStructuralSurfaceMemberBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuralSurfaceMember();}}

static class IfcTypeObjectBuilder implements ClassBuilder
{public Object instance(){return new IfcTypeObject();}}

static class IfcFacetedBrepWithVoidsBuilder implements ClassBuilder
{public Object instance(){return new IfcFacetedBrepWithVoids();}}

static class IfcMaterialLayerBuilder implements ClassBuilder
{public Object instance(){return new IfcMaterialLayer();}}

static class IfcFaceSurfaceBuilder implements ClassBuilder
{public Object instance(){return new IfcFaceSurface();}}

static class IfcCircleProfileDefBuilder implements ClassBuilder
{public Object instance(){return new IfcCircleProfileDef();}}

static class IfcRelAssociatesProfilePropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcRelAssociatesProfileProperties();}}

static class IfcFanTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcFanType();}}

static class IfcGeometricCurveSetBuilder implements ClassBuilder
{public Object instance(){return new IfcGeometricCurveSet();}}

static class IfcEvaporativeCoolerTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcEvaporativeCoolerType();}}

static class IfcOrderActionBuilder implements ClassBuilder
{public Object instance(){return new IfcOrderAction();}}

static class File_DescriptionBuilder implements ClassBuilder
{public Object instance(){return new File_Description();}}

static class IfcRationalBezierCurveBuilder implements ClassBuilder
{public Object instance(){return new IfcRationalBezierCurve();}}

static class IfcFlowFittingBuilder implements ClassBuilder
{public Object instance(){return new IfcFlowFitting();}}

static class IfcEllipseBuilder implements ClassBuilder
{public Object instance(){return new IfcEllipse();}}

static class IfcWaterPropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcWaterProperties();}}

static class IfcPipeFittingTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcPipeFittingType();}}

static class IfcEnergyConversionDeviceBuilder implements ClassBuilder
{public Object instance(){return new IfcEnergyConversionDevice();}}

static class IfcPolygonalBoundedHalfSpaceBuilder implements ClassBuilder
{public Object instance(){return new IfcPolygonalBoundedHalfSpace();}}

static class IfcSectionedSpineBuilder implements ClassBuilder
{public Object instance(){return new IfcSectionedSpine();}}

static class IfcVirtualGridIntersectionBuilder implements ClassBuilder
{public Object instance(){return new IfcVirtualGridIntersection();}}

static class IfcGroupBuilder implements ClassBuilder
{public Object instance(){return new IfcGroup();}}

static class IfcLightSourceDirectionalBuilder implements ClassBuilder
{public Object instance(){return new IfcLightSourceDirectional();}}

static class IfcPolyLoopBuilder implements ClassBuilder
{public Object instance(){return new IfcPolyLoop();}}

static class IfcActionRequestBuilder implements ClassBuilder
{public Object instance(){return new IfcActionRequest();}}

static class IfcRelAssignsToResourceBuilder implements ClassBuilder
{public Object instance(){return new IfcRelAssignsToResource();}}

static class IfcRectangularTrimmedSurfaceBuilder implements ClassBuilder
{public Object instance(){return new IfcRectangularTrimmedSurface();}}

static class IfcDraughtingPreDefinedColourBuilder implements ClassBuilder
{public Object instance(){return new IfcDraughtingPreDefinedColour();}}

static class IfcLocalTimeBuilder implements ClassBuilder
{public Object instance(){return new IfcLocalTime();}}

static class IfcEdgeLoopBuilder implements ClassBuilder
{public Object instance(){return new IfcEdgeLoop();}}

static class IfcSurfaceStyleShadingBuilder implements ClassBuilder
{public Object instance(){return new IfcSurfaceStyleShading();}}

static class IfcDimensionCurveTerminatorBuilder implements ClassBuilder
{public Object instance(){return new IfcDimensionCurveTerminator();}}

static class IfcConnectionPointEccentricityBuilder implements ClassBuilder
{public Object instance(){return new IfcConnectionPointEccentricity();}}

static class IfcComplexPropertyBuilder implements ClassBuilder
{public Object instance(){return new IfcComplexProperty();}}

static class IfcConstraintClassificationRelationshipBuilder implements ClassBuilder
{public Object instance(){return new IfcConstraintClassificationRelationship();}}

static class IfcOrganizationBuilder implements ClassBuilder
{public Object instance(){return new IfcOrganization();}}

static class IfcQuantityTimeBuilder implements ClassBuilder
{public Object instance(){return new IfcQuantityTime();}}

static class IfcApplicationBuilder implements ClassBuilder
{public Object instance(){return new IfcApplication();}}

static class IfcOpenShellBuilder implements ClassBuilder
{public Object instance(){return new IfcOpenShell();}}

static class IfcTimeSeriesValueBuilder implements ClassBuilder
{public Object instance(){return new IfcTimeSeriesValue();}}

static class IfcHeatExchangerTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcHeatExchangerType();}}

static class IfcHygroscopicMaterialPropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcHygroscopicMaterialProperties();}}

static class IfcRelConnectsWithEccentricityBuilder implements ClassBuilder
{public Object instance(){return new IfcRelConnectsWithEccentricity();}}

static class IfcColumnTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcColumnType();}}

static class IfcProductDefinitionShapeBuilder implements ClassBuilder
{public Object instance(){return new IfcProductDefinitionShape();}}

static class IfcBlobTextureBuilder implements ClassBuilder
{public Object instance(){return new IfcBlobTexture();}}

static class IfcStructuralSteelProfilePropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuralSteelProfileProperties();}}

static class IfcActorRoleBuilder implements ClassBuilder
{public Object instance(){return new IfcActorRole();}}

static class IfcTableRowBuilder implements ClassBuilder
{public Object instance(){return new IfcTableRow();}}

static class IfcVertexPointBuilder implements ClassBuilder
{public Object instance(){return new IfcVertexPoint();}}

static class IfcPresentationStyleAssignmentBuilder implements ClassBuilder
{public Object instance(){return new IfcPresentationStyleAssignment();}}

static class IfcCurveStyleFontBuilder implements ClassBuilder
{public Object instance(){return new IfcCurveStyleFont();}}

static class IfcMappedItemBuilder implements ClassBuilder
{public Object instance(){return new IfcMappedItem();}}

static class IfcConstructionProductResourceBuilder implements ClassBuilder
{public Object instance(){return new IfcConstructionProductResource();}}

static class IfcPlanarExtentBuilder implements ClassBuilder
{public Object instance(){return new IfcPlanarExtent();}}

static class IfcDimensionCurveBuilder implements ClassBuilder
{public Object instance(){return new IfcDimensionCurve();}}

static class IfcMemberBuilder implements ClassBuilder
{public Object instance(){return new IfcMember();}}

static class IfcDamperTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcDamperType();}}

static class IfcLShapeProfileDefBuilder implements ClassBuilder
{public Object instance(){return new IfcLShapeProfileDef();}}

static class IfcRelSpaceBoundaryBuilder implements ClassBuilder
{public Object instance(){return new IfcRelSpaceBoundary();}}

static class IfcTextStyleForDefinedFontBuilder implements ClassBuilder
{public Object instance(){return new IfcTextStyleForDefinedFont();}}

static class IfcMaterialLayerSetBuilder implements ClassBuilder
{public Object instance(){return new IfcMaterialLayerSet();}}

static class IfcElectricDistributionPointBuilder implements ClassBuilder
{public Object instance(){return new IfcElectricDistributionPoint();}}

static class IfcMaterialClassificationRelationshipBuilder implements ClassBuilder
{public Object instance(){return new IfcMaterialClassificationRelationship();}}

static class IfcCondenserTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcCondenserType();}}

static class IfcChillerTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcChillerType();}}

static class IfcMaterialLayerSetUsageBuilder implements ClassBuilder
{public Object instance(){return new IfcMaterialLayerSetUsage();}}

static class IfcImageTextureBuilder implements ClassBuilder
{public Object instance(){return new IfcImageTexture();}}

static class IfcVirtualElementBuilder implements ClassBuilder
{public Object instance(){return new IfcVirtualElement();}}

static class IfcSurfaceStyleWithTexturesBuilder implements ClassBuilder
{public Object instance(){return new IfcSurfaceStyleWithTextures();}}

static class IfcActorBuilder implements ClassBuilder
{public Object instance(){return new IfcActor();}}

static class IfcRelContainedInSpatialStructureBuilder implements ClassBuilder
{public Object instance(){return new IfcRelContainedInSpatialStructure();}}

static class IfcCircleHollowProfileDefBuilder implements ClassBuilder
{public Object instance(){return new IfcCircleHollowProfileDef();}}

static class IfcTerminatorSymbolBuilder implements ClassBuilder
{public Object instance(){return new IfcTerminatorSymbol();}}

static class IfcConstructionEquipmentResourceBuilder implements ClassBuilder
{public Object instance(){return new IfcConstructionEquipmentResource();}}

static class IfcPermitBuilder implements ClassBuilder
{public Object instance(){return new IfcPermit();}}

static class IfcStyledItemBuilder implements ClassBuilder
{public Object instance(){return new IfcStyledItem();}}

static class IfcOrganizationRelationshipBuilder implements ClassBuilder
{public Object instance(){return new IfcOrganizationRelationship();}}

static class IfcDocumentElectronicFormatBuilder implements ClassBuilder
{public Object instance(){return new IfcDocumentElectronicFormat();}}

static class IfcAlarmTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcAlarmType();}}

static class IfcRelAssignsToProductBuilder implements ClassBuilder
{public Object instance(){return new IfcRelAssignsToProduct();}}

static class IfcPropertyEnumerationBuilder implements ClassBuilder
{public Object instance(){return new IfcPropertyEnumeration();}}

static class IfcDiscreteAccessoryTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcDiscreteAccessoryType();}}

static class IfcFaceBuilder implements ClassBuilder
{public Object instance(){return new IfcFace();}}

static class IfcRelAssignsToGroupBuilder implements ClassBuilder
{public Object instance(){return new IfcRelAssignsToGroup();}}

static class IfcTubeBundleTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcTubeBundleType();}}

static class IfcAssetBuilder implements ClassBuilder
{public Object instance(){return new IfcAsset();}}

static class IfcSurfaceStyleRenderingBuilder implements ClassBuilder
{public Object instance(){return new IfcSurfaceStyleRendering();}}

static class IfcProcedureBuilder implements ClassBuilder
{public Object instance(){return new IfcProcedure();}}

static class IfcCurrencyRelationshipBuilder implements ClassBuilder
{public Object instance(){return new IfcCurrencyRelationship();}}

static class IfcDoorBuilder implements ClassBuilder
{public Object instance(){return new IfcDoor();}}

static class IfcVertexBasedTextureMapBuilder implements ClassBuilder
{public Object instance(){return new IfcVertexBasedTextureMap();}}

static class IfcSpaceTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcSpaceType();}}

static class IfcMechanicalMaterialPropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcMechanicalMaterialProperties();}}

static class IfcPointOnCurveBuilder implements ClassBuilder
{public Object instance(){return new IfcPointOnCurve();}}

static class IfcConditionBuilder implements ClassBuilder
{public Object instance(){return new IfcCondition();}}

static class IfcStructuralPointActionBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuralPointAction();}}

static class IfcRelConnectsStructuralMemberBuilder implements ClassBuilder
{public Object instance(){return new IfcRelConnectsStructuralMember();}}

static class IfcAnnotationFillAreaOccurrenceBuilder implements ClassBuilder
{public Object instance(){return new IfcAnnotationFillAreaOccurrence();}}

static class IfcFaceBasedSurfaceModelBuilder implements ClassBuilder
{public Object instance(){return new IfcFaceBasedSurfaceModel();}}

static class IfcRampFlightBuilder implements ClassBuilder
{public Object instance(){return new IfcRampFlight();}}

static class IfcOwnerHistoryBuilder implements ClassBuilder
{public Object instance(){return new IfcOwnerHistory();}}

static class IfcDocumentInformationRelationshipBuilder implements ClassBuilder
{public Object instance(){return new IfcDocumentInformationRelationship();}}

static class File_NameBuilder implements ClassBuilder
{public Object instance(){return new File_Name();}}

static class IfcRampFlightTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcRampFlightType();}}

static class IfcDimensionalExponentsBuilder implements ClassBuilder
{public Object instance(){return new IfcDimensionalExponents();}}

static class IfcRelVoidsElementBuilder implements ClassBuilder
{public Object instance(){return new IfcRelVoidsElement();}}

static class IfcDerivedUnitBuilder implements ClassBuilder
{public Object instance(){return new IfcDerivedUnit();}}

static class IfcStructuralCurveMemberVaryingBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuralCurveMemberVarying();}}

static class IfcRelAssociatesAppliedValueBuilder implements ClassBuilder
{public Object instance(){return new IfcRelAssociatesAppliedValue();}}

static class IfcProjectOrderRecordBuilder implements ClassBuilder
{public Object instance(){return new IfcProjectOrderRecord();}}

static class IfcServiceLifeBuilder implements ClassBuilder
{public Object instance(){return new IfcServiceLife();}}

static class IfcQuantityWeightBuilder implements ClassBuilder
{public Object instance(){return new IfcQuantityWeight();}}

static class IfcCurveStyleFontAndScalingBuilder implements ClassBuilder
{public Object instance(){return new IfcCurveStyleFontAndScaling();}}

static class IfcPropertyReferenceValueBuilder implements ClassBuilder
{public Object instance(){return new IfcPropertyReferenceValue();}}

static class IfcElementAssemblyBuilder implements ClassBuilder
{public Object instance(){return new IfcElementAssembly();}}

static class IfcFaceOuterBoundBuilder implements ClassBuilder
{public Object instance(){return new IfcFaceOuterBound();}}

static class IfcPreDefinedPointMarkerSymbolBuilder implements ClassBuilder
{public Object instance(){return new IfcPreDefinedPointMarkerSymbol();}}

static class IfcOpeningElementBuilder implements ClassBuilder
{public Object instance(){return new IfcOpeningElement();}}

static class IfcLightSourcePositionalBuilder implements ClassBuilder
{public Object instance(){return new IfcLightSourcePositional();}}

static class IfcRelDefinesByTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcRelDefinesByType();}}

static class IfcSubContractResourceBuilder implements ClassBuilder
{public Object instance(){return new IfcSubContractResource();}}

static class IfcClassificationItemBuilder implements ClassBuilder
{public Object instance(){return new IfcClassificationItem();}}

static class IfcOccupantBuilder implements ClassBuilder
{public Object instance(){return new IfcOccupant();}}

static class IfcSurfaceStyleBuilder implements ClassBuilder
{public Object instance(){return new IfcSurfaceStyle();}}

static class IfcLaborResourceBuilder implements ClassBuilder
{public Object instance(){return new IfcLaborResource();}}

static class IfcRelAssociatesConstraintBuilder implements ClassBuilder
{public Object instance(){return new IfcRelAssociatesConstraint();}}

static class IfcTextStyleWithBoxCharacteristicsBuilder implements ClassBuilder
{public Object instance(){return new IfcTextStyleWithBoxCharacteristics();}}

static class IfcCostItemBuilder implements ClassBuilder
{public Object instance(){return new IfcCostItem();}}

static class IfcFurnishingElementTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcFurnishingElementType();}}

static class IfcSphereBuilder implements ClassBuilder
{public Object instance(){return new IfcSphere();}}

static class IfcBuildingElementProxyBuilder implements ClassBuilder
{public Object instance(){return new IfcBuildingElementProxy();}}

static class IfcRelAssignsTasksBuilder implements ClassBuilder
{public Object instance(){return new IfcRelAssignsTasks();}}

static class IfcEquipmentStandardBuilder implements ClassBuilder
{public Object instance(){return new IfcEquipmentStandard();}}

static class IfcBoundingBoxBuilder implements ClassBuilder
{public Object instance(){return new IfcBoundingBox();}}

static class IfcLibraryInformationBuilder implements ClassBuilder
{public Object instance(){return new IfcLibraryInformation();}}

static class IfcVertexLoopBuilder implements ClassBuilder
{public Object instance(){return new IfcVertexLoop();}}

static class IfcRadiusDimensionBuilder implements ClassBuilder
{public Object instance(){return new IfcRadiusDimension();}}

static class IfcPropertyConstraintRelationshipBuilder implements ClassBuilder
{public Object instance(){return new IfcPropertyConstraintRelationship();}}

static class IfcBoxedHalfSpaceBuilder implements ClassBuilder
{public Object instance(){return new IfcBoxedHalfSpace();}}

static class IfcLightSourceGoniometricBuilder implements ClassBuilder
{public Object instance(){return new IfcLightSourceGoniometric();}}

static class IfcTendonAnchorBuilder implements ClassBuilder
{public Object instance(){return new IfcTendonAnchor();}}

static class IfcControllerTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcControllerType();}}

static class IfcTransformerTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcTransformerType();}}

static class IfcPreDefinedDimensionSymbolBuilder implements ClassBuilder
{public Object instance(){return new IfcPreDefinedDimensionSymbol();}}

static class IfcCShapeProfileDefBuilder implements ClassBuilder
{public Object instance(){return new IfcCShapeProfileDef();}}

static class IfcTextureMapBuilder implements ClassBuilder
{public Object instance(){return new IfcTextureMap();}}

static class IfcFuelPropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcFuelProperties();}}

static class IfcCableCarrierSegmentTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcCableCarrierSegmentType();}}

static class IfcSpaceThermalLoadPropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcSpaceThermalLoadProperties();}}

static class IfcRelAssignsToControlBuilder implements ClassBuilder
{public Object instance(){return new IfcRelAssignsToControl();}}

static class IfcElectricalBasePropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcElectricalBaseProperties();}}

static class IfcTaskBuilder implements ClassBuilder
{public Object instance(){return new IfcTask();}}

static class IfcSubedgeBuilder implements ClassBuilder
{public Object instance(){return new IfcSubedge();}}

static class IfcDistributionChamberElementTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcDistributionChamberElementType();}}

static class IfcPlateBuilder implements ClassBuilder
{public Object instance(){return new IfcPlate();}}

static class IfcQuantityCountBuilder implements ClassBuilder
{public Object instance(){return new IfcQuantityCount();}}

static class IfcMoveBuilder implements ClassBuilder
{public Object instance(){return new IfcMove();}}

static class IfcRelAssociatesApprovalBuilder implements ClassBuilder
{public Object instance(){return new IfcRelAssociatesApproval();}}

static class IfcFlowTreatmentDeviceBuilder implements ClassBuilder
{public Object instance(){return new IfcFlowTreatmentDevice();}}

static class IfcCoveringTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcCoveringType();}}

static class IfcLineBuilder implements ClassBuilder
{public Object instance(){return new IfcLine();}}

static class IfcStructuralLoadPlanarForceBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuralLoadPlanarForce();}}

static class IfcStairFlightBuilder implements ClassBuilder
{public Object instance(){return new IfcStairFlight();}}

static class IfcSanitaryTerminalTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcSanitaryTerminalType();}}

static class IfcRelConnectsStructuralElementBuilder implements ClassBuilder
{public Object instance(){return new IfcRelConnectsStructuralElement();}}

static class IfcLightDistributionDataBuilder implements ClassBuilder
{public Object instance(){return new IfcLightDistributionData();}}

static class IfcWasteTerminalTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcWasteTerminalType();}}

static class IfcSlabBuilder implements ClassBuilder
{public Object instance(){return new IfcSlab();}}

static class IfcFillAreaStyleTilesBuilder implements ClassBuilder
{public Object instance(){return new IfcFillAreaStyleTiles();}}

static class IfcEquipmentElementBuilder implements ClassBuilder
{public Object instance(){return new IfcEquipmentElement();}}

static class IfcDuctFittingTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcDuctFittingType();}}

static class IfcSurfaceStyleLightingBuilder implements ClassBuilder
{public Object instance(){return new IfcSurfaceStyleLighting();}}

static class IfcSiteBuilder implements ClassBuilder
{public Object instance(){return new IfcSite();}}

static class IfcTrimmedCurveBuilder implements ClassBuilder
{public Object instance(){return new IfcTrimmedCurve();}}

static class IfcRepresentationContextBuilder implements ClassBuilder
{public Object instance(){return new IfcRepresentationContext();}}

static class IfcColourRgbBuilder implements ClassBuilder
{public Object instance(){return new IfcColourRgb();}}

static class IfcRelAssociatesMaterialBuilder implements ClassBuilder
{public Object instance(){return new IfcRelAssociatesMaterial();}}

static class IfcOrientedEdgeBuilder implements ClassBuilder
{public Object instance(){return new IfcOrientedEdge();}}

static class IfcElectricGeneratorTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcElectricGeneratorType();}}

static class IfcRelAssociatesClassificationBuilder implements ClassBuilder
{public Object instance(){return new IfcRelAssociatesClassification();}}

static class IfcDuctSilencerTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcDuctSilencerType();}}

static class IfcDimensionPairBuilder implements ClassBuilder
{public Object instance(){return new IfcDimensionPair();}}

static class IfcEnergyPropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcEnergyProperties();}}

static class IfcPileBuilder implements ClassBuilder
{public Object instance(){return new IfcPile();}}

static class IfcCraneRailFShapeProfileDefBuilder implements ClassBuilder
{public Object instance(){return new IfcCraneRailFShapeProfileDef();}}

static class IfcMaterialBuilder implements ClassBuilder
{public Object instance(){return new IfcMaterial();}}

static class IfcCircleBuilder implements ClassBuilder
{public Object instance(){return new IfcCircle();}}

static class IfcStructuralLoadSingleForceWarpingBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuralLoadSingleForceWarping();}}

static class IfcWallStandardCaseBuilder implements ClassBuilder
{public Object instance(){return new IfcWallStandardCase();}}

static class IfcArbitraryProfileDefWithVoidsBuilder implements ClassBuilder
{public Object instance(){return new IfcArbitraryProfileDefWithVoids();}}

static class IfcTextStyleBuilder implements ClassBuilder
{public Object instance(){return new IfcTextStyle();}}

static class IfcMechanicalSteelMaterialPropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcMechanicalSteelMaterialProperties();}}

static class IfcBeamBuilder implements ClassBuilder
{public Object instance(){return new IfcBeam();}}

static class IfcOffsetCurve3DBuilder implements ClassBuilder
{public Object instance(){return new IfcOffsetCurve3D();}}

static class IfcApprovalBuilder implements ClassBuilder
{public Object instance(){return new IfcApproval();}}

static class IfcProductsOfCombustionPropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcProductsOfCombustionProperties();}}

static class IfcCurtainWallBuilder implements ClassBuilder
{public Object instance(){return new IfcCurtainWall();}}

static class IfcDistributionElementTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcDistributionElementType();}}

static class IfcIShapeProfileDefBuilder implements ClassBuilder
{public Object instance(){return new IfcIShapeProfileDef();}}

static class IfcArbitraryOpenProfileDefBuilder implements ClassBuilder
{public Object instance(){return new IfcArbitraryOpenProfileDef();}}

static class IfcValveTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcValveType();}}

static class IfcExtrudedAreaSolidBuilder implements ClassBuilder
{public Object instance(){return new IfcExtrudedAreaSolid();}}

static class IfcActuatorTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcActuatorType();}}

static class IfcTextureCoordinateGeneratorBuilder implements ClassBuilder
{public Object instance(){return new IfcTextureCoordinateGenerator();}}

static class IfcPropertySetBuilder implements ClassBuilder
{public Object instance(){return new IfcPropertySet();}}

static class IfcProtectiveDeviceTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcProtectiveDeviceType();}}

static class IfcContextDependentUnitBuilder implements ClassBuilder
{public Object instance(){return new IfcContextDependentUnit();}}

static class IfcConstraintRelationshipBuilder implements ClassBuilder
{public Object instance(){return new IfcConstraintRelationship();}}

static class IfcOffsetCurve2DBuilder implements ClassBuilder
{public Object instance(){return new IfcOffsetCurve2D();}}

static class IfcBoundaryNodeConditionWarpingBuilder implements ClassBuilder
{public Object instance(){return new IfcBoundaryNodeConditionWarping();}}

static class IfcFurnitureStandardBuilder implements ClassBuilder
{public Object instance(){return new IfcFurnitureStandard();}}

static class IfcVertexBuilder implements ClassBuilder
{public Object instance(){return new IfcVertex();}}

static class IfcReinforcingMeshBuilder implements ClassBuilder
{public Object instance(){return new IfcReinforcingMesh();}}

static class IfcZoneBuilder implements ClassBuilder
{public Object instance(){return new IfcZone();}}

static class IfcRampBuilder implements ClassBuilder
{public Object instance(){return new IfcRamp();}}

static class IfcStairFlightTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcStairFlightType();}}

static class IfcMemberTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcMemberType();}}

static class IfcTextureVertexBuilder implements ClassBuilder
{public Object instance(){return new IfcTextureVertex();}}

static class IfcTankTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcTankType();}}

static class IfcCurveBoundedPlaneBuilder implements ClassBuilder
{public Object instance(){return new IfcCurveBoundedPlane();}}

static class IfcEdgeBuilder implements ClassBuilder
{public Object instance(){return new IfcEdge();}}

static class IfcAnnotationFillAreaBuilder implements ClassBuilder
{public Object instance(){return new IfcAnnotationFillArea();}}

static class IfcServiceLifeFactorBuilder implements ClassBuilder
{public Object instance(){return new IfcServiceLifeFactor();}}

static class IfcMeasureWithUnitBuilder implements ClassBuilder
{public Object instance(){return new IfcMeasureWithUnit();}}

static class IfcMonetaryUnitBuilder implements ClassBuilder
{public Object instance(){return new IfcMonetaryUnit();}}

static class IfcTextLiteralWithExtentBuilder implements ClassBuilder
{public Object instance(){return new IfcTextLiteralWithExtent();}}

static class IfcQuantityAreaBuilder implements ClassBuilder
{public Object instance(){return new IfcQuantityArea();}}

static class IfcUnitAssignmentBuilder implements ClassBuilder
{public Object instance(){return new IfcUnitAssignment();}}

static class IfcRelSchedulesCostItemsBuilder implements ClassBuilder
{public Object instance(){return new IfcRelSchedulesCostItems();}}

static class IfcVibrationIsolatorTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcVibrationIsolatorType();}}

static class IfcRelAggregatesBuilder implements ClassBuilder
{public Object instance(){return new IfcRelAggregates();}}

static class IfcScheduleTimeControlBuilder implements ClassBuilder
{public Object instance(){return new IfcScheduleTimeControl();}}

static class IfcCompositeCurveBuilder implements ClassBuilder
{public Object instance(){return new IfcCompositeCurve();}}

static class IfcRelAssignsToProcessBuilder implements ClassBuilder
{public Object instance(){return new IfcRelAssignsToProcess();}}

static class IfcDraughtingCalloutBuilder implements ClassBuilder
{public Object instance(){return new IfcDraughtingCallout();}}

static class IfcRelConnectsElementsBuilder implements ClassBuilder
{public Object instance(){return new IfcRelConnectsElements();}}

static class IfcTextStyleFontModelBuilder implements ClassBuilder
{public Object instance(){return new IfcTextStyleFontModel();}}

static class IfcPropertyEnumeratedValueBuilder implements ClassBuilder
{public Object instance(){return new IfcPropertyEnumeratedValue();}}

static class IfcCartesianPointBuilder implements ClassBuilder
{public Object instance(){return new IfcCartesianPoint();}}

static class IfcBuildingBuilder implements ClassBuilder
{public Object instance(){return new IfcBuilding();}}

static class IfcEllipseProfileDefBuilder implements ClassBuilder
{public Object instance(){return new IfcEllipseProfileDef();}}

static class IfcConversionBasedUnitBuilder implements ClassBuilder
{public Object instance(){return new IfcConversionBasedUnit();}}

static class IfcShellBasedSurfaceModelBuilder implements ClassBuilder
{public Object instance(){return new IfcShellBasedSurfaceModel();}}

static class IfcAnnotationCurveOccurrenceBuilder implements ClassBuilder
{public Object instance(){return new IfcAnnotationCurveOccurrence();}}

static class IfcAirTerminalBoxTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcAirTerminalBoxType();}}

static class IfcSwitchingDeviceTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcSwitchingDeviceType();}}

static class IfcPlateTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcPlateType();}}

static class IfcProjectOrderBuilder implements ClassBuilder
{public Object instance(){return new IfcProjectOrder();}}

static class IfcDoorStyleBuilder implements ClassBuilder
{public Object instance(){return new IfcDoorStyle();}}

static class IfcExternallyDefinedSurfaceStyleBuilder implements ClassBuilder
{public Object instance(){return new IfcExternallyDefinedSurfaceStyle();}}

static class IfcSpaceBuilder implements ClassBuilder
{public Object instance(){return new IfcSpace();}}

static class IfcPlaneBuilder implements ClassBuilder
{public Object instance(){return new IfcPlane();}}

static class IfcSurfaceStyleRefractionBuilder implements ClassBuilder
{public Object instance(){return new IfcSurfaceStyleRefraction();}}

static class IfcBoundaryFaceConditionBuilder implements ClassBuilder
{public Object instance(){return new IfcBoundaryFaceCondition();}}

static class IfcFlowSegmentBuilder implements ClassBuilder
{public Object instance(){return new IfcFlowSegment();}}

static class IfcTransportElementTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcTransportElementType();}}

static class IfcSurfaceOfLinearExtrusionBuilder implements ClassBuilder
{public Object instance(){return new IfcSurfaceOfLinearExtrusion();}}

static class IfcRectangleHollowProfileDefBuilder implements ClassBuilder
{public Object instance(){return new IfcRectangleHollowProfileDef();}}

static class IfcStackTerminalTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcStackTerminalType();}}

static class IfcDistributionElementBuilder implements ClassBuilder
{public Object instance(){return new IfcDistributionElement();}}

static class IfcLocalPlacementBuilder implements ClassBuilder
{public Object instance(){return new IfcLocalPlacement();}}

static class IfcUnitaryEquipmentTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcUnitaryEquipmentType();}}

static class IfcBezierCurveBuilder implements ClassBuilder
{public Object instance(){return new IfcBezierCurve();}}

static class IfcExternallyDefinedTextFontBuilder implements ClassBuilder
{public Object instance(){return new IfcExternallyDefinedTextFont();}}

static class IfcFilterTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcFilterType();}}

static class IfcStructuralPointConnectionBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuralPointConnection();}}

static class IfcDefinedSymbolBuilder implements ClassBuilder
{public Object instance(){return new IfcDefinedSymbol();}}

static class IfcCostScheduleBuilder implements ClassBuilder
{public Object instance(){return new IfcCostSchedule();}}

static class IfcBoundaryNodeConditionBuilder implements ClassBuilder
{public Object instance(){return new IfcBoundaryNodeCondition();}}

static class IfcRailingBuilder implements ClassBuilder
{public Object instance(){return new IfcRailing();}}

static class IfcCartesianTransformationOperator3DnonUniformBuilder implements ClassBuilder
{public Object instance(){return new IfcCartesianTransformationOperator3DnonUniform();}}

static class IfcQuantityVolumeBuilder implements ClassBuilder
{public Object instance(){return new IfcQuantityVolume();}}

static class IfcRepresentationMapBuilder implements ClassBuilder
{public Object instance(){return new IfcRepresentationMap();}}

static class IfcElectricMotorTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcElectricMotorType();}}

static class IfcLightSourceAmbientBuilder implements ClassBuilder
{public Object instance(){return new IfcLightSourceAmbient();}}

static class IfcTextLiteralBuilder implements ClassBuilder
{public Object instance(){return new IfcTextLiteral();}}

static class File_SchemaBuilder implements ClassBuilder
{public Object instance(){return new File_Schema();}}

static class IfcStructuralCurveConnectionBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuralCurveConnection();}}

static class IfcElectricHeaterTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcElectricHeaterType();}}

static class IfcElementQuantityBuilder implements ClassBuilder
{public Object instance(){return new IfcElementQuantity();}}

static class IfcRegularTimeSeriesBuilder implements ClassBuilder
{public Object instance(){return new IfcRegularTimeSeries();}}

static class IfcGridAxisBuilder implements ClassBuilder
{public Object instance(){return new IfcGridAxis();}}

static class IfcCraneRailAShapeProfileDefBuilder implements ClassBuilder
{public Object instance(){return new IfcCraneRailAShapeProfileDef();}}

static class IfcTendonBuilder implements ClassBuilder
{public Object instance(){return new IfcTendon();}}

static class IfcRoundedEdgeFeatureBuilder implements ClassBuilder
{public Object instance(){return new IfcRoundedEdgeFeature();}}

static class IfcWallTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcWallType();}}

static class IfcBoundedSurfaceBuilder implements ClassBuilder
{public Object instance(){return new IfcBoundedSurface();}}

static class IfcPersonBuilder implements ClassBuilder
{public Object instance(){return new IfcPerson();}}

static class IfcWorkPlanBuilder implements ClassBuilder
{public Object instance(){return new IfcWorkPlan();}}

static class IfcBooleanResultBuilder implements ClassBuilder
{public Object instance(){return new IfcBooleanResult();}}

static class IfcFastenerTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcFastenerType();}}

static class IfcTelecomAddressBuilder implements ClassBuilder
{public Object instance(){return new IfcTelecomAddress();}}

static class IfcAnnotationTextOccurrenceBuilder implements ClassBuilder
{public Object instance(){return new IfcAnnotationTextOccurrence();}}

static class IfcPropertyTableValueBuilder implements ClassBuilder
{public Object instance(){return new IfcPropertyTableValue();}}

static class IfcIrregularTimeSeriesValueBuilder implements ClassBuilder
{public Object instance(){return new IfcIrregularTimeSeriesValue();}}

static class IfcDocumentInformationBuilder implements ClassBuilder
{public Object instance(){return new IfcDocumentInformation();}}

static class IfcPipeSegmentTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcPipeSegmentType();}}

static class IfcRelInteractionRequirementsBuilder implements ClassBuilder
{public Object instance(){return new IfcRelInteractionRequirements();}}

static class IfcReinforcementBarPropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcReinforcementBarProperties();}}

static class IfcConditionCriterionBuilder implements ClassBuilder
{public Object instance(){return new IfcConditionCriterion();}}

static class IfcStructuralPlanarActionVaryingBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuralPlanarActionVarying();}}

static class IfcArbitraryClosedProfileDefBuilder implements ClassBuilder
{public Object instance(){return new IfcArbitraryClosedProfileDef();}}

static class IfcClassificationReferenceBuilder implements ClassBuilder
{public Object instance(){return new IfcClassificationReference();}}

static class IfcMotorConnectionTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcMotorConnectionType();}}

static class IfcPermeableCoveringPropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcPermeableCoveringProperties();}}

static class IfcWindowBuilder implements ClassBuilder
{public Object instance(){return new IfcWindow();}}

static class IfcAxis1PlacementBuilder implements ClassBuilder
{public Object instance(){return new IfcAxis1Placement();}}

static class IfcDerivedUnitElementBuilder implements ClassBuilder
{public Object instance(){return new IfcDerivedUnitElement();}}

static class IfcRibPlateProfilePropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcRibPlateProfileProperties();}}

static class IfcRelServicesBuildingsBuilder implements ClassBuilder
{public Object instance(){return new IfcRelServicesBuildings();}}

static class IfcBoundaryEdgeConditionBuilder implements ClassBuilder
{public Object instance(){return new IfcBoundaryEdgeCondition();}}

static class IfcEnvironmentalImpactValueBuilder implements ClassBuilder
{public Object instance(){return new IfcEnvironmentalImpactValue();}}

static class IfcCsgSolidBuilder implements ClassBuilder
{public Object instance(){return new IfcCsgSolid();}}

static class IfcCableSegmentTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcCableSegmentType();}}

static class IfcApprovalActorRelationshipBuilder implements ClassBuilder
{public Object instance(){return new IfcApprovalActorRelationship();}}

static class IfcFacetedBrepBuilder implements ClassBuilder
{public Object instance(){return new IfcFacetedBrep();}}

static class IfcCalendarDateBuilder implements ClassBuilder
{public Object instance(){return new IfcCalendarDate();}}

static class IfcOneDirectionRepeatFactorBuilder implements ClassBuilder
{public Object instance(){return new IfcOneDirectionRepeatFactor();}}

static class IfcRelConnectsPortToElementBuilder implements ClassBuilder
{public Object instance(){return new IfcRelConnectsPortToElement();}}

static class IfcReinforcementDefinitionPropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcReinforcementDefinitionProperties();}}

static class IfcDerivedProfileDefBuilder implements ClassBuilder
{public Object instance(){return new IfcDerivedProfileDef();}}

static class IfcRelCoversBldgElementsBuilder implements ClassBuilder
{public Object instance(){return new IfcRelCoversBldgElements();}}

static class IfcRelAssociatesBuilder implements ClassBuilder
{public Object instance(){return new IfcRelAssociates();}}

static class IfcClassificationNotationBuilder implements ClassBuilder
{public Object instance(){return new IfcClassificationNotation();}}

static class IfcPresentationLayerAssignmentBuilder implements ClassBuilder
{public Object instance(){return new IfcPresentationLayerAssignment();}}

static class IfcStructuralCurveMemberBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuralCurveMember();}}

static class IfcConstraintAggregationRelationshipBuilder implements ClassBuilder
{public Object instance(){return new IfcConstraintAggregationRelationship();}}

static class IfcStructuralLoadLinearForceBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuralLoadLinearForce();}}

static class IfcSoundValueBuilder implements ClassBuilder
{public Object instance(){return new IfcSoundValue();}}

static class IfcRectangularPyramidBuilder implements ClassBuilder
{public Object instance(){return new IfcRectangularPyramid();}}

static class IfcRelDefinesByPropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcRelDefinesByProperties();}}

static class IfcPixelTextureBuilder implements ClassBuilder
{public Object instance(){return new IfcPixelTexture();}}

static class IfcTextStyleTextModelBuilder implements ClassBuilder
{public Object instance(){return new IfcTextStyleTextModel();}}

static class IfcStairBuilder implements ClassBuilder
{public Object instance(){return new IfcStair();}}

static class IfcShapeRepresentationBuilder implements ClassBuilder
{public Object instance(){return new IfcShapeRepresentation();}}

static class IfcEdgeCurveBuilder implements ClassBuilder
{public Object instance(){return new IfcEdgeCurve();}}

static class IfcConnectionCurveGeometryBuilder implements ClassBuilder
{public Object instance(){return new IfcConnectionCurveGeometry();}}

static class IfcPerformanceHistoryBuilder implements ClassBuilder
{public Object instance(){return new IfcPerformanceHistory();}}

static class IfcStructuralLoadSingleForceBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuralLoadSingleForce();}}

static class IfcMaterialDefinitionRepresentationBuilder implements ClassBuilder
{public Object instance(){return new IfcMaterialDefinitionRepresentation();}}

static class IfcCooledBeamTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcCooledBeamType();}}

static class IfcDiameterDimensionBuilder implements ClassBuilder
{public Object instance(){return new IfcDiameterDimension();}}

static class IfcTypeProductBuilder implements ClassBuilder
{public Object instance(){return new IfcTypeProduct();}}

static class IfcStructuralLoadSingleDisplacementBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuralLoadSingleDisplacement();}}

static class IfcWindowPanelPropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcWindowPanelProperties();}}

static class IfcRailingTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcRailingType();}}

static class IfcElectricTimeControlTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcElectricTimeControlType();}}

static class IfcPreDefinedTerminatorSymbolBuilder implements ClassBuilder
{public Object instance(){return new IfcPreDefinedTerminatorSymbol();}}

static class IfcGeometricSetBuilder implements ClassBuilder
{public Object instance(){return new IfcGeometricSet();}}

static class IfcThermalMaterialPropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcThermalMaterialProperties();}}

static class IfcAirToAirHeatRecoveryTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcAirToAirHeatRecoveryType();}}

static class IfcEvaporatorTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcEvaporatorType();}}

static class IfcClassificationNotationFacetBuilder implements ClassBuilder
{public Object instance(){return new IfcClassificationNotationFacet();}}

static class IfcPropertyListValueBuilder implements ClassBuilder
{public Object instance(){return new IfcPropertyListValue();}}

static class IfcDoorLiningPropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcDoorLiningProperties();}}

static class IfcBooleanClippingResultBuilder implements ClassBuilder
{public Object instance(){return new IfcBooleanClippingResult();}}

static class IfcCenterLineProfileDefBuilder implements ClassBuilder
{public Object instance(){return new IfcCenterLineProfileDef();}}

static class IfcDraughtingPreDefinedCurveFontBuilder implements ClassBuilder
{public Object instance(){return new IfcDraughtingPreDefinedCurveFont();}}

static class IfcCoveringBuilder implements ClassBuilder
{public Object instance(){return new IfcCovering();}}

static class IfcPropertyDependencyRelationshipBuilder implements ClassBuilder
{public Object instance(){return new IfcPropertyDependencyRelationship();}}

static class IfcApprovalRelationshipBuilder implements ClassBuilder
{public Object instance(){return new IfcApprovalRelationship();}}

static class IfcReferencesValueDocumentBuilder implements ClassBuilder
{public Object instance(){return new IfcReferencesValueDocument();}}

static class IfcStructuralResultGroupBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuralResultGroup();}}

static class IfcAppliedValueRelationshipBuilder implements ClassBuilder
{public Object instance(){return new IfcAppliedValueRelationship();}}

static class IfcFailureConnectionConditionBuilder implements ClassBuilder
{public Object instance(){return new IfcFailureConnectionCondition();}}

static class IfcBeamTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcBeamType();}}

static class IfcColumnBuilder implements ClassBuilder
{public Object instance(){return new IfcColumn();}}

static class IfcRectangleProfileDefBuilder implements ClassBuilder
{public Object instance(){return new IfcRectangleProfileDef();}}

static class IfcCurveStyleFontPatternBuilder implements ClassBuilder
{public Object instance(){return new IfcCurveStyleFontPattern();}}

static class IfcLightIntensityDistributionBuilder implements ClassBuilder
{public Object instance(){return new IfcLightIntensityDistribution();}}

static class IfcPointOnSurfaceBuilder implements ClassBuilder
{public Object instance(){return new IfcPointOnSurface();}}

static class IfcStructuralSurfaceConnectionBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuralSurfaceConnection();}}

static class IfcStructuralPointReactionBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuralPointReaction();}}

static class IfcLampTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcLampType();}}

static class IfcSpaceHeaterTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcSpaceHeaterType();}}

static class IfcStyledRepresentationBuilder implements ClassBuilder
{public Object instance(){return new IfcStyledRepresentation();}}

static class IfcStructuredDimensionCalloutBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuredDimensionCallout();}}

static class IfcAnnotationSymbolOccurrenceBuilder implements ClassBuilder
{public Object instance(){return new IfcAnnotationSymbolOccurrence();}}

static class IfcPathBuilder implements ClassBuilder
{public Object instance(){return new IfcPath();}}

static class IfcStructuralProfilePropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuralProfileProperties();}}

static class IfcElectricApplianceTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcElectricApplianceType();}}

static class IfcPolylineBuilder implements ClassBuilder
{public Object instance(){return new IfcPolyline();}}

static class IfcProxyBuilder implements ClassBuilder
{public Object instance(){return new IfcProxy();}}

static class IfcExternallyDefinedHatchStyleBuilder implements ClassBuilder
{public Object instance(){return new IfcExternallyDefinedHatchStyle();}}

static class IfcTwoDirectionRepeatFactorBuilder implements ClassBuilder
{public Object instance(){return new IfcTwoDirectionRepeatFactor();}}

static class IfcSectionPropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcSectionProperties();}}

static class IfcElectricFlowStorageDeviceTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcElectricFlowStorageDeviceType();}}

static class IfcGasTerminalTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcGasTerminalType();}}

static class IfcGeometricRepresentationSubContextBuilder implements ClassBuilder
{public Object instance(){return new IfcGeometricRepresentationSubContext();}}

static class IfcStructuralLoadSingleDisplacementDistortionBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuralLoadSingleDisplacementDistortion();}}

static class IfcBoilerTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcBoilerType();}}

static class IfcFaceBoundBuilder implements ClassBuilder
{public Object instance(){return new IfcFaceBound();}}

static class IfcRelReferencedInSpatialStructureBuilder implements ClassBuilder
{public Object instance(){return new IfcRelReferencedInSpatialStructure();}}

static class IfcSystemFurnitureElementTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcSystemFurnitureElementType();}}

static class IfcAnnotationBuilder implements ClassBuilder
{public Object instance(){return new IfcAnnotation();}}

static class IfcTrapeziumProfileDefBuilder implements ClassBuilder
{public Object instance(){return new IfcTrapeziumProfileDef();}}

static class IfcFillAreaStyleBuilder implements ClassBuilder
{public Object instance(){return new IfcFillAreaStyle();}}

static class IfcConnectionPointGeometryBuilder implements ClassBuilder
{public Object instance(){return new IfcConnectionPointGeometry();}}

static class IfcRelAssociatesDocumentBuilder implements ClassBuilder
{public Object instance(){return new IfcRelAssociatesDocument();}}

static class IfcPresentationLayerWithStyleBuilder implements ClassBuilder
{public Object instance(){return new IfcPresentationLayerWithStyle();}}

static class IfcFlowControllerBuilder implements ClassBuilder
{public Object instance(){return new IfcFlowController();}}

static class IfcRoofBuilder implements ClassBuilder
{public Object instance(){return new IfcRoof();}}

static class IfcVectorBuilder implements ClassBuilder
{public Object instance(){return new IfcVector();}}

static class IfcGeneralProfilePropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcGeneralProfileProperties();}}

static class IfcHumidifierTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcHumidifierType();}}

static class IfcWindowLiningPropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcWindowLiningProperties();}}

static class IfcDiscreteAccessoryBuilder implements ClassBuilder
{public Object instance(){return new IfcDiscreteAccessory();}}

static class IfcBuildingElementProxyTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcBuildingElementProxyType();}}

static class IfcHalfSpaceSolidBuilder implements ClassBuilder
{public Object instance(){return new IfcHalfSpaceSolid();}}

static class IfcFurnishingElementBuilder implements ClassBuilder
{public Object instance(){return new IfcFurnishingElement();}}

static class IfcAirTerminalTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcAirTerminalType();}}

static class IfcApprovalPropertyRelationshipBuilder implements ClassBuilder
{public Object instance(){return new IfcApprovalPropertyRelationship();}}

static class IfcMechanicalConcreteMaterialPropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcMechanicalConcreteMaterialProperties();}}

static class IfcAngularDimensionBuilder implements ClassBuilder
{public Object instance(){return new IfcAngularDimension();}}

static class IfcPumpTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcPumpType();}}

static class IfcLoopBuilder implements ClassBuilder
{public Object instance(){return new IfcLoop();}}

static class IfcFurnitureTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcFurnitureType();}}

static class IfcPlanarBoxBuilder implements ClassBuilder
{public Object instance(){return new IfcPlanarBox();}}

static class IfcPostalAddressBuilder implements ClassBuilder
{public Object instance(){return new IfcPostalAddress();}}

static class IfcObjectiveBuilder implements ClassBuilder
{public Object instance(){return new IfcObjective();}}

static class IfcRevolvedAreaSolidBuilder implements ClassBuilder
{public Object instance(){return new IfcRevolvedAreaSolid();}}

static class IfcRelAssignsToProjectOrderBuilder implements ClassBuilder
{public Object instance(){return new IfcRelAssignsToProjectOrder();}}

static class IfcFlowTerminalBuilder implements ClassBuilder
{public Object instance(){return new IfcFlowTerminal();}}

static class IfcProjectBuilder implements ClassBuilder
{public Object instance(){return new IfcProject();}}

static class IfcWallBuilder implements ClassBuilder
{public Object instance(){return new IfcWall();}}

static class IfcRelProjectsElementBuilder implements ClassBuilder
{public Object instance(){return new IfcRelProjectsElement();}}

static class IfcDocumentReferenceBuilder implements ClassBuilder
{public Object instance(){return new IfcDocumentReference();}}

static class IfcRelAssociatesLibraryBuilder implements ClassBuilder
{public Object instance(){return new IfcRelAssociatesLibrary();}}

static class IfcCompressorTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcCompressorType();}}

static class IfcSpaceProgramBuilder implements ClassBuilder
{public Object instance(){return new IfcSpaceProgram();}}

static class IfcBuildingStoreyBuilder implements ClassBuilder
{public Object instance(){return new IfcBuildingStorey();}}

static class IfcSweptDiskSolidBuilder implements ClassBuilder
{public Object instance(){return new IfcSweptDiskSolid();}}

static class IfcTimeSeriesReferenceRelationshipBuilder implements ClassBuilder
{public Object instance(){return new IfcTimeSeriesReferenceRelationship();}}

static class Ifc2DCompositeCurveBuilder implements ClassBuilder
{public Object instance(){return new Ifc2DCompositeCurve();}}

static class IfcCartesianTransformationOperator2DnonUniformBuilder implements ClassBuilder
{public Object instance(){return new IfcCartesianTransformationOperator2DnonUniform();}}

static class IfcChamferEdgeFeatureBuilder implements ClassBuilder
{public Object instance(){return new IfcChamferEdgeFeature();}}

static class IfcProjectionElementBuilder implements ClassBuilder
{public Object instance(){return new IfcProjectionElement();}}

static class IfcMechanicalFastenerBuilder implements ClassBuilder
{public Object instance(){return new IfcMechanicalFastener();}}

static class IfcTransportElementBuilder implements ClassBuilder
{public Object instance(){return new IfcTransportElement();}}

static class IfcTimeSeriesScheduleBuilder implements ClassBuilder
{public Object instance(){return new IfcTimeSeriesSchedule();}}

static class IfcRelSequenceBuilder implements ClassBuilder
{public Object instance(){return new IfcRelSequence();}}

static class IfcFillAreaStyleTileSymbolWithStyleBuilder implements ClassBuilder
{public Object instance(){return new IfcFillAreaStyleTileSymbolWithStyle();}}

static class IfcFluidFlowPropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcFluidFlowProperties();}}

static class IfcFootingBuilder implements ClassBuilder
{public Object instance(){return new IfcFooting();}}

static class IfcProductRepresentationBuilder implements ClassBuilder
{public Object instance(){return new IfcProductRepresentation();}}

static class IfcConnectedFaceSetBuilder implements ClassBuilder
{public Object instance(){return new IfcConnectedFaceSet();}}

static class IfcBuildingElementPartBuilder implements ClassBuilder
{public Object instance(){return new IfcBuildingElementPart();}}

static class IfcAxis2Placement3DBuilder implements ClassBuilder
{public Object instance(){return new IfcAxis2Placement3D();}}

static class IfcLightSourceSpotBuilder implements ClassBuilder
{public Object instance(){return new IfcLightSourceSpot();}}

static class IfcStructuralPlanarActionBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuralPlanarAction();}}

static class IfcProjectionCurveBuilder implements ClassBuilder
{public Object instance(){return new IfcProjectionCurve();}}

static class IfcLinearDimensionBuilder implements ClassBuilder
{public Object instance(){return new IfcLinearDimension();}}

static class IfcClassificationBuilder implements ClassBuilder
{public Object instance(){return new IfcClassification();}}

static class IfcFlowMovingDeviceBuilder implements ClassBuilder
{public Object instance(){return new IfcFlowMovingDevice();}}

static class IfcUShapeProfileDefBuilder implements ClassBuilder
{public Object instance(){return new IfcUShapeProfileDef();}}

static class IfcPersonAndOrganizationBuilder implements ClassBuilder
{public Object instance(){return new IfcPersonAndOrganization();}}

static class IfcMetricBuilder implements ClassBuilder
{public Object instance(){return new IfcMetric();}}

static class IfcLightFixtureTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcLightFixtureType();}}

static class IfcSurfaceOfRevolutionBuilder implements ClassBuilder
{public Object instance(){return new IfcSurfaceOfRevolution();}}

static class IfcCableCarrierFittingTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcCableCarrierFittingType();}}

static class IfcStructuralLoadTemperatureBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuralLoadTemperature();}}

static class IfcRightCircularCylinderBuilder implements ClassBuilder
{public Object instance(){return new IfcRightCircularCylinder();}}

static class IfcCostValueBuilder implements ClassBuilder
{public Object instance(){return new IfcCostValue();}}

static class IfcElectricalCircuitBuilder implements ClassBuilder
{public Object instance(){return new IfcElectricalCircuit();}}

static class IfcFillAreaStyleHatchingBuilder implements ClassBuilder
{public Object instance(){return new IfcFillAreaStyleHatching();}}

static class IfcShapeAspectBuilder implements ClassBuilder
{public Object instance(){return new IfcShapeAspect();}}

static class IfcRelConnectsPathElementsBuilder implements ClassBuilder
{public Object instance(){return new IfcRelConnectsPathElements();}}

static class IfcConstructionMaterialResourceBuilder implements ClassBuilder
{public Object instance(){return new IfcConstructionMaterialResource();}}

static class IfcIrregularTimeSeriesBuilder implements ClassBuilder
{public Object instance(){return new IfcIrregularTimeSeries();}}

static class IfcStructuralLinearActionBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuralLinearAction();}}

static class IfcGeneralMaterialPropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcGeneralMaterialProperties();}}

static class IfcCompositeCurveSegmentBuilder implements ClassBuilder
{public Object instance(){return new IfcCompositeCurveSegment();}}

static class IfcGridPlacementBuilder implements ClassBuilder
{public Object instance(){return new IfcGridPlacement();}}

static class IfcOpticalMaterialPropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcOpticalMaterialProperties();}}

static class IfcDateAndTimeBuilder implements ClassBuilder
{public Object instance(){return new IfcDateAndTime();}}

static class IfcStructuralLinearActionVaryingBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuralLinearActionVarying();}}

static class IfcExternallyDefinedSymbolBuilder implements ClassBuilder
{public Object instance(){return new IfcExternallyDefinedSymbol();}}

static class IfcDistributionControlElementBuilder implements ClassBuilder
{public Object instance(){return new IfcDistributionControlElement();}}

static class IfcInventoryBuilder implements ClassBuilder
{public Object instance(){return new IfcInventory();}}

static class IfcTShapeProfileDefBuilder implements ClassBuilder
{public Object instance(){return new IfcTShapeProfileDef();}}

static class IfcDoorPanelPropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcDoorPanelProperties();}}

static class IfcDimensionCalloutRelationshipBuilder implements ClassBuilder
{public Object instance(){return new IfcDimensionCalloutRelationship();}}

static class IfcGridBuilder implements ClassBuilder
{public Object instance(){return new IfcGrid();}}

static class IfcDistributionChamberElementBuilder implements ClassBuilder
{public Object instance(){return new IfcDistributionChamberElement();}}

static class IfcFlowInstrumentTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcFlowInstrumentType();}}

static class IfcTopologyRepresentationBuilder implements ClassBuilder
{public Object instance(){return new IfcTopologyRepresentation();}}

static class IfcDistributionPortBuilder implements ClassBuilder
{public Object instance(){return new IfcDistributionPort();}}

static class IfcAnnotationSurfaceOccurrenceBuilder implements ClassBuilder
{public Object instance(){return new IfcAnnotationSurfaceOccurrence();}}

static class IfcCurveStyleBuilder implements ClassBuilder
{public Object instance(){return new IfcCurveStyle();}}

static class IfcRelFlowControlElementsBuilder implements ClassBuilder
{public Object instance(){return new IfcRelFlowControlElements();}}

static class IfcCartesianTransformationOperator3DBuilder implements ClassBuilder
{public Object instance(){return new IfcCartesianTransformationOperator3D();}}

static class IfcStructuralSurfaceMemberVaryingBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuralSurfaceMemberVarying();}}

static class IfcFastenerBuilder implements ClassBuilder
{public Object instance(){return new IfcFastener();}}

static class IfcStructuralAnalysisModelBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuralAnalysisModel();}}

static class IfcLibraryReferenceBuilder implements ClassBuilder
{public Object instance(){return new IfcLibraryReference();}}

static class IfcRelaxationBuilder implements ClassBuilder
{public Object instance(){return new IfcRelaxation();}}

static class IfcSoundPropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcSoundProperties();}}

static class IfcClosedShellBuilder implements ClassBuilder
{public Object instance(){return new IfcClosedShell();}}

static class IfcRelFillsElementBuilder implements ClassBuilder
{public Object instance(){return new IfcRelFillsElement();}}

static class IfcCurtainWallTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcCurtainWallType();}}

static class IfcAxis2Placement2DBuilder implements ClassBuilder
{public Object instance(){return new IfcAxis2Placement2D();}}

static class IfcFlowMeterTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcFlowMeterType();}}

static class IfcWorkScheduleBuilder implements ClassBuilder
{public Object instance(){return new IfcWorkSchedule();}}

static class IfcConnectionPortGeometryBuilder implements ClassBuilder
{public Object instance(){return new IfcConnectionPortGeometry();}}

static class IfcCrewResourceBuilder implements ClassBuilder
{public Object instance(){return new IfcCrewResource();}}

static class IfcSlabTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcSlabType();}}

static class IfcRelConnectsStructuralActivityBuilder implements ClassBuilder
{public Object instance(){return new IfcRelConnectsStructuralActivity();}}

static class IfcCoolingTowerTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcCoolingTowerType();}}

static class IfcOutletTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcOutletType();}}

static class IfcRelCoversSpacesBuilder implements ClassBuilder
{public Object instance(){return new IfcRelCoversSpaces();}}

static class IfcRelConnectsWithRealizingElementsBuilder implements ClassBuilder
{public Object instance(){return new IfcRelConnectsWithRealizingElements();}}

static class IfcDraughtingCalloutRelationshipBuilder implements ClassBuilder
{public Object instance(){return new IfcDraughtingCalloutRelationship();}}

static class IfcSectionReinforcementPropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcSectionReinforcementProperties();}}

static class IfcElectricalElementBuilder implements ClassBuilder
{public Object instance(){return new IfcElectricalElement();}}

static class IfcSystemBuilder implements ClassBuilder
{public Object instance(){return new IfcSystem();}}

static class IfcRelOverridesPropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcRelOverridesProperties();}}

static class IfcWindowStyleBuilder implements ClassBuilder
{public Object instance(){return new IfcWindowStyle();}}

static class IfcSymbolStyleBuilder implements ClassBuilder
{public Object instance(){return new IfcSymbolStyle();}}

static class IfcSensorTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcSensorType();}}

static class IfcStructuralLoadGroupBuilder implements ClassBuilder
{public Object instance(){return new IfcStructuralLoadGroup();}}

static class IfcMechanicalFastenerTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcMechanicalFastenerType();}}

static class IfcConnectionSurfaceGeometryBuilder implements ClassBuilder
{public Object instance(){return new IfcConnectionSurfaceGeometry();}}

static class IfcRelNestsBuilder implements ClassBuilder
{public Object instance(){return new IfcRelNests();}}

static class IfcMaterialListBuilder implements ClassBuilder
{public Object instance(){return new IfcMaterialList();}}

static class IfcDistributionFlowElementBuilder implements ClassBuilder
{public Object instance(){return new IfcDistributionFlowElement();}}

static class IfcTableBuilder implements ClassBuilder
{public Object instance(){return new IfcTable();}}

static class IfcRoundedRectangleProfileDefBuilder implements ClassBuilder
{public Object instance(){return new IfcRoundedRectangleProfileDef();}}

static class IfcAnnotationSurfaceBuilder implements ClassBuilder
{public Object instance(){return new IfcAnnotationSurface();}}

static class IfcExtendedMaterialPropertiesBuilder implements ClassBuilder
{public Object instance(){return new IfcExtendedMaterialProperties();}}

static class IfcClassificationItemRelationshipBuilder implements ClassBuilder
{public Object instance(){return new IfcClassificationItemRelationship();}}

static class IfcPropertyBoundedValueBuilder implements ClassBuilder
{public Object instance(){return new IfcPropertyBoundedValue();}}

static class IfcCompositeProfileDefBuilder implements ClassBuilder
{public Object instance(){return new IfcCompositeProfileDef();}}

static class IfcGeometricRepresentationContextBuilder implements ClassBuilder
{public Object instance(){return new IfcGeometricRepresentationContext();}}

static class IfcAsymmetricIShapeProfileDefBuilder implements ClassBuilder
{public Object instance(){return new IfcAsymmetricIShapeProfileDef();}}

static class IfcRelConnectsPortsBuilder implements ClassBuilder
{public Object instance(){return new IfcRelConnectsPorts();}}

static class IfcQuantityLengthBuilder implements ClassBuilder
{public Object instance(){return new IfcQuantityLength();}}

static class IfcSlippageConnectionConditionBuilder implements ClassBuilder
{public Object instance(){return new IfcSlippageConnectionCondition();}}

static class IfcRepresentationBuilder implements ClassBuilder
{public Object instance(){return new IfcRepresentation();}}

static class IfcRelOccupiesSpacesBuilder implements ClassBuilder
{public Object instance(){return new IfcRelOccupiesSpaces();}}

static class IfcZShapeProfileDefBuilder implements ClassBuilder
{public Object instance(){return new IfcZShapeProfileDef();}}

static class IfcDimensionCurveDirectedCalloutBuilder implements ClassBuilder
{public Object instance(){return new IfcDimensionCurveDirectedCallout();}}

static class IfcSurfaceCurveSweptAreaSolidBuilder implements ClassBuilder
{public Object instance(){return new IfcSurfaceCurveSweptAreaSolid();}}

static class IfcPropertySingleValueBuilder implements ClassBuilder
{public Object instance(){return new IfcPropertySingleValue();}}

static class IfcCoilTypeBuilder implements ClassBuilder
{public Object instance(){return new IfcCoilType();}}

static class IfcBlockBuilder implements ClassBuilder
{public Object instance(){return new IfcBlock();}}

static class IfcCartesianTransformationOperator2DBuilder implements ClassBuilder
{public Object instance(){return new IfcCartesianTransformationOperator2D();}}

static class IfcPhysicalComplexQuantityBuilder implements ClassBuilder
{public Object instance(){return new IfcPhysicalComplexQuantity();}}

static class INTEGERBuilder implements ClassBuilder
{public Object instance(){return new INTEGER();}}

static class DOUBLEBuilder implements ClassBuilder
{public Object instance(){return new DOUBLE();}}

static class BOOLEANBuilder implements ClassBuilder
{public Object instance(){return new BOOLEAN();}}

static class BINARYBuilder implements ClassBuilder
{public Object instance(){return new BINARY();}}

static class REALBuilder implements ClassBuilder
{public Object instance(){return new REAL();}}

static class STRINGBuilder implements ClassBuilder
{public Object instance(){return new STRING();}}

static class LOGICALBuilder implements ClassBuilder
{public Object instance(){return new LOGICAL();}}

}

