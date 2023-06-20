package triathlon.network.protobuffprotoccol;

import triathlon.model.Participant;
import triathlon.model.Referee;
import triathlon.model.Result;

import java.sql.Ref;

public class ProtoUtils {
    public static TriathlonProtobufs.TriathlonRequest createLoginRequest(Referee referee){
        TriathlonProtobufs.Referee theReferee = TriathlonProtobufs.Referee.newBuilder().setId(referee.getId()).setUsername(referee.getUsername()).setPasswrod(referee.getPassword()).setFirstName(referee.getFirst_name()).setLastName(referee.getLast_name()).setActivity(referee.getActivity()).build();
        TriathlonProtobufs.TriathlonRequest request = TriathlonProtobufs.TriathlonRequest.newBuilder().setType(TriathlonProtobufs.TriathlonRequest.Type.Login)
                .setReferee(theReferee).build();
        return request;
    }

    public static TriathlonProtobufs.TriathlonRequest createLogoutRequest(Referee referee){
        TriathlonProtobufs.Referee theReferee = TriathlonProtobufs.Referee.newBuilder().setId(referee.getId()).setUsername(referee.getUsername()).setPasswrod(referee.getPassword()).setFirstName(referee.getFirst_name()).setLastName(referee.getLast_name()).setActivity(referee.getActivity()).build();
        TriathlonProtobufs.TriathlonRequest request = TriathlonProtobufs.TriathlonRequest.newBuilder().setType(TriathlonProtobufs.TriathlonRequest.Type.Logout)
                .setReferee(theReferee).build();
        return request;
    }

    public static TriathlonProtobufs.TriathlonRequest createAddScoreRequest(Result result){
        TriathlonProtobufs.Result theResult = TriathlonProtobufs.Result.newBuilder().
                setParticipantId(result.getParticipant().getId())
                .setRefereeId(result.getReferee().getId())
                .setPoints(result.getPoints())
                .setActivity(result.getActivity()).build();
        TriathlonProtobufs.TriathlonRequest request = TriathlonProtobufs.TriathlonRequest.newBuilder()
                .setType(TriathlonProtobufs.TriathlonRequest.Type.AddScore)
                .setResult(theResult).build();
        return request;
    }

    public static TriathlonProtobufs.TriathlonRequest createGetTopPartRequest(){
        TriathlonProtobufs.TriathlonRequest request = TriathlonProtobufs.TriathlonRequest.newBuilder()
                .setType(TriathlonProtobufs.TriathlonRequest.Type.GetTop)
                .build();
        return request;
    }

    public static TriathlonProtobufs.TriathlonRequest createGetNotedRequest(Referee referee){
        TriathlonProtobufs.Referee theReferee = TriathlonProtobufs.Referee.newBuilder().setId(referee.getId()).setUsername(referee.getUsername()).setPasswrod(referee.getPassword()).setFirstName(referee.getFirst_name()).setLastName(referee.getLast_name()).setActivity(referee.getActivity()).build();
        TriathlonProtobufs.TriathlonRequest request = TriathlonProtobufs.TriathlonRequest.newBuilder()
                .setType(TriathlonProtobufs.TriathlonRequest.Type.GetNoted)
                .setReferee(theReferee).build();
        return request;
    }

    public static TriathlonProtobufs.TriathlonRequest createNotNotedRequest(Referee referee){
        TriathlonProtobufs.Referee theReferee = TriathlonProtobufs.Referee.newBuilder().setId(referee.getId()).setUsername(referee.getUsername()).setPasswrod(referee.getPassword()).setFirstName(referee.getFirst_name()).setLastName(referee.getLast_name()).setActivity(referee.getActivity()).build();
        TriathlonProtobufs.TriathlonRequest request = TriathlonProtobufs.TriathlonRequest.newBuilder()
                .setType(TriathlonProtobufs.TriathlonRequest.Type.GetNotNoted)
                .setReferee(theReferee).build();
        return request;
    }

    public static TriathlonProtobufs.TriathlonRequest createGetPartRequest(int id){
        TriathlonProtobufs.TriathlonRequest request = TriathlonProtobufs.TriathlonRequest.newBuilder()
                .setType(TriathlonProtobufs.TriathlonRequest.Type.GetPart)
                .setId(id).build();
        return request;
    }




    // RESPONSE-URI

    public static TriathlonProtobufs.TriathlonResponse createOkResponse(){
        TriathlonProtobufs.TriathlonResponse response = TriathlonProtobufs.TriathlonResponse.newBuilder()
                .setType(TriathlonProtobufs.TriathlonResponse.Type.Ok).build();
        return response;
    }

    public static TriathlonProtobufs.TriathlonResponse createErrorResponse(String text){
        TriathlonProtobufs.TriathlonResponse response = TriathlonProtobufs.TriathlonResponse.newBuilder()
                .setType(TriathlonProtobufs.TriathlonResponse.Type.Error)
                .setError(text).build();
        return response;
    }

    public static TriathlonProtobufs.TriathlonResponse createAddScoreResponse(Result result){
        TriathlonProtobufs.Result theResult = TriathlonProtobufs.Result.newBuilder().
                setParticipantId(result.getParticipant().getId())
                .setRefereeId(result.getReferee().getId())
                .setPoints(result.getPoints())
                .setActivity(result.getActivity()).build();
        TriathlonProtobufs.TriathlonResponse response = TriathlonProtobufs.TriathlonResponse.newBuilder()
                .setType(TriathlonProtobufs.TriathlonResponse.Type.NewScore)
                .setResult(theResult).build();
        return response;
    }

//    public static TriathlonProtobufs.TriathlonResponse createGetTopResponse(Iterable<Participant> participants){
//        TriathlonProtobufs.TriathlonResponse.Builder response = TriathlonProtobufs.TriathlonResponse.newBuilder()
//                .setType(TriathlonProtobufs.TriathlonResponse.Type.GetTop);
//        for(Participant part: participants){
//            TriathlonProtobufs.Participant participant = TriathlonProtobufs.Participant.newBuilder()
//                    .setId(part.getId())
//                    .setFirstName(part.getFirst_name())
//                    .setLastName(part.getLast_name())
//                    .setPoints(part.getPoints()).build();
//            response.addParticipants(participant);
//        }
//        return response.build();
//    }

    public static TriathlonProtobufs.TriathlonResponse createGetTopResponse(Iterable<Participant> participants){
        TriathlonProtobufs.TriathlonResponse.Builder response = TriathlonProtobufs.TriathlonResponse.newBuilder()
                .setType(TriathlonProtobufs.TriathlonResponse.Type.GetTop);
        for(Participant part: participants){
            TriathlonProtobufs.Participant participant = TriathlonProtobufs.Participant.newBuilder()
                    .setId(part.getId())
                    .setFirstName(part.getFirst_name())
                    .setLastName(part.getLast_name())
                    .setPoints(part.getPoints())
                    .build();
            response.addParticipants(participant);
        }
        return response.build();
    }

    public static TriathlonProtobufs.TriathlonResponse createGetNotedPartResponse(Participant[] participants){
        TriathlonProtobufs.TriathlonResponse.Builder response = TriathlonProtobufs.TriathlonResponse.newBuilder()
                .setType(TriathlonProtobufs.TriathlonResponse.Type.GetNoted);
        for(Participant part: participants){
            TriathlonProtobufs.Participant participant = TriathlonProtobufs.Participant.newBuilder()
                    .setId(part.getId())
                    .setFirstName(part.getFirst_name())
                    .setLastName(part.getLast_name())
                    .setPoints(part.getPoints())
                    .build();
            response.addParticipants(participant);
        }
        return response.build();
    }

    public static TriathlonProtobufs.TriathlonResponse createGetNotNotedPartResponse(Participant[] participants){
        TriathlonProtobufs.TriathlonResponse.Builder response = TriathlonProtobufs.TriathlonResponse.newBuilder()
                .setType(TriathlonProtobufs.TriathlonResponse.Type.GetNotNoted);
        for(Participant part: participants){
            TriathlonProtobufs.Participant participant = TriathlonProtobufs.Participant.newBuilder()
                    .setId(part.getId())
                    .setFirstName(part.getFirst_name())
                    .setLastName(part.getLast_name())
                    .setPoints(part.getPoints())
                    .build();
            response.addParticipants(participant);
        }
        return response.build();
    }

    public static TriathlonProtobufs.TriathlonResponse createGetPart(Participant participant){
        TriathlonProtobufs.Participant theParticipant = TriathlonProtobufs.Participant.newBuilder().
                setId(participant.getId())
                .setFirstName(participant.getFirst_name())
                .setLastName(participant.getLast_name())
                .setPoints(participant.getPoints()).build();

        TriathlonProtobufs.TriathlonResponse response = TriathlonProtobufs.TriathlonResponse.newBuilder()
                .setType(TriathlonProtobufs.TriathlonResponse.Type.Part)
                .setParticipant(theParticipant).build();
        return response;
    }

    public static String getError(TriathlonProtobufs.TriathlonResponse response){
        String errorMsg = response.getError();
        return errorMsg;
    }

//    public static Referee getReferee(TriathlonProtobufs.TriathlonResponse response){
//        Referee referee = new Referee();
//        referee.setId(response.getReferee().getId());
//        return Referee
//    }

}
