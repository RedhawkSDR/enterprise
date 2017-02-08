/*
 * This file is protected by Copyright. Please refer to the COPYRIGHT file
 * distributed with this source distribution.
 *
 * This file is part of REDHAWK __REDHAWK_PROJECT__.
 *
 * REDHAWK __REDHAWK_PROJECT__ is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * REDHAWK __REDHAWK_PROJECT__ is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package redhawk;

import org.junit.Ignore;

@Ignore("This doesn't even appear to be a test...")
public class RedhawkTester {

    
    private static final String FRONTEND_TUNER_STATUS = "FRONTEND::tuner_status";
    private static final String FRONTEND_TUNER_STATUS_ALLOCATED = "allocated";
    private static final String FRONTEND_TUNER_STATUS_ALLOCATION_ID = FRONTEND_TUNER_STATUS + "::allocation_id_csv";
    private static final String FRONTEND_TUNER_STATUS_AVAILABLE_FREQUENCY = FRONTEND_TUNER_STATUS + "::available_frequency";
    private static final String FRONTEND_TUNER_STATUS_CENTER_FREQUENCY = FRONTEND_TUNER_STATUS + "::center_frequency";
    private static final String FRONTEND_TUNER_STATUS_CHANNEL_NUMBER = "channel_number";
    private static final String FRONTEND_TUNER_STATUS_GROUP_ID = FRONTEND_TUNER_STATUS + "::group_id";
    private static final String FRONTEND_TUNER_STATUS_RF_FLOW_ID = FRONTEND_TUNER_STATUS + "::rf_flow_id";
    private static final String FRONTEND_TUNER_STATUS_TUNER_TYPE = FRONTEND_TUNER_STATUS + "::tuner_type";    
    
    
    public static void main(String[] args) throws Exception {
//        System.out.println("BEFORE TIME : " + new Date());
//        Redhawk redhawk = new RedhawkDriver("localhost", 2809, );
//        
//        RedhawkDomainManager redhawkDomainManager = redhawk.connect();
//        RedhawkDeviceManager deviceManager = redhawkDomainManager.getDeviceManagerByName("BDX.*");
//        RedhawkDevice device = deviceManager.getDeviceByName("BDX.*");
//        
//        System.out.println("GETTING PROPERTY : " + new Date());
//        
//        Map<String, RedhawkProperty> frontendTunerStatuses = device.getProperties();
//
//        System.out.println("RESOLVED STATUSES : " + new Date());
        
//        Map<String, List<String>> tunerMap = new HashMap<String, List<String>>();
//
//        if (frontendTunerStatuses != null) {
//                for (RedhawkStruct tunerStatus : frontendTunerStatuses.getStructs()) {
//                        if (tunerStatus != null) {
//                                tunerStatus.getValue(FRONTEND_TUNER_STATUS_ALLOCATED);
//                                tunerStatus.getValue(FRONTEND_TUNER_STATUS_CHANNEL_NUMBER);
//                                tunerStatus.getValue(FRONTEND_TUNER_STATUS_CENTER_FREQUENCY);
//                                tunerStatus.getValue(FRONTEND_TUNER_STATUS_GROUP_ID);
//                                String rfFlowId = tunerStatus.getValue(FRONTEND_TUNER_STATUS_RF_FLOW_ID) + "";
//                                
//
//                                if (tunerMap.get(rfFlowId) == null) {
//                                        List<String> frequencies = new ArrayList<String>();
//                                        tunerMap.put(rfFlowId, frequencies);
//                                } else {
//                                        tunerMap.get(rfFlowId).add("TEST");
//                                }
//                        }
//                }
//        }
//
//        System.out.println("DONE : " + new Date());
//        
//        int cnt = 0;
//        for (String key : tunerMap.keySet()) {
//                System.out.println("HERE");
//        }
//
//        
//        System.out.println("REALLY DONE : " + new Date());
        
        
        
//        RedhawkDomainManager manager = redhawk.connect();
//        
//        RedhawkApplication application = manager.getApplicationByName(".*mission_manager.*");
//        
//        
//        
//        
//        
//        RedhawkComponent component = application.getComponentByName(".*mwpb_tasker.*");
//        
//        RedhawkStructSequence seq = component.getProperty("search_tasking_api");
//        
//        Map<String, Object> elementToAdd = new HashMap<String, Object>();
//        
//        seq.addStructToSequence(elementToAdd);
        
//        RedhawkStruct struct = component.getProperty("myStruct");
//        struct.setValue("theSimpleInTheStruct", "HELLO JOHN FSFSDF");
//        System.out.println(struct.getValue("theSimpleInTheStruct"));

        
//        RedhawkStruct struct = seq.getStructByPropertyAndValue("mySimpleInTheThing", "DUMB5 VALUE");
//        struct.setValue("mySimpleInTheThing", "DUMB10 VALUE");
//        System.out.println(struct.getValue("mySimpleInTheThing"));
//        RedhawkStructSequence seq2 = (RedhawkStructSequence) component.getProperties().get("myStructSequence");
//        System.out.println(seq2.getStructByPropertyAndValue("mySimpleInTheThing", "DUMB10 VALUE").getValue("mySimpleInTheThing"));
        
        
//        System.out.println(component.getProperties().get("myStruct"));
//        seq.removeStructWithPropertyThatMatches("mySimpleInTheThing", "RYANS_SSTEST");
//        System.out.println(seq.getValue("theSimpleInTheStruct"));
        
        
//        System.out.println(component.getProperties().get("myStructSequence"));
        
//        for(RedhawkDevice device : manager.getAllDevices()){
//            RedhawkStructSequence frontEndTunerStatuses = device.getProperty("FRONTEND::tuner_status");
//            
//            if(frontEndTunerStatuses != null){
//                List<RedhawkStruct> frontEndStructs = frontEndTunerStatuses.getStructsByPropertyAndValue("FRONTEND::tuner_status::tuner_type", "CHANNELIZER");
//                if(frontEndStructs != null){
//                    for(RedhawkStruct s : frontEndStructs){
//                        //add frequency
//                    }
//                    
//                    System.out.println("FOUND:::::::: " + frontEndStructs.size());
//                }
//            }
//        }
//        
//        
//        redhawk.disconnect();
        
        
    }
    
    
}
