import React, {useState} from "react";

function ResultRow({participant, deleteFunc, updateFunc}){
    const [updatedFName, setUpdatedFName] = useState(participant.first_name);
    const [updatedLName, setUpdatedLName] = useState(participant.last_name);
    const [updatedPoints, setUpdatedPoints] = useState(participant.points);
    const [isModified, setIsModified] = useState(false);

    function handleDelete(event){
        console.log('delete button pentru '+ participant.id);
        deleteFunc(participant.id);
    }

    function handleUpdate(){
        console.log("UPDATE: NEW F_NAME " + updatedFName);
        participant.first_name = updatedFName;
        participant.last_name = updatedLName;
        participant.points = updatedPoints;
        console.log('update btn pt ' + participant.id);
        updateFunc(participant);
    }

    const handleEdit = () =>{
        setIsModified(true);
    };

    const handleCancel = () => {
        setIsModified(false);
        setUpdatedFName(participant.first_name);
        setUpdatedLName(participant.last_name);
        setUpdatedPoints(participant.points);
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        handleUpdate();
        setIsModified(false);
    };

    return(
        <tr>
            <td>
                {participant.id}
            </td>


            <td>
                {isModified ? (
                    <input
                        type="text"
                        value={updatedFName}
                        onChange={(e) => setUpdatedFName(e.target.value)}
                    />
                ) : (
                    participant.first_name
                )}
            </td>


            <td>
                {isModified ? (
                    <input
                        type="text"
                        value={updatedLName}
                        onChange={(e) => setUpdatedLName(e.target.value)}
                    />
                ) : (
                    participant.last_name
                )}
            </td>


            <td>
                {isModified ? (
                    <input
                        type="text"
                        value={updatedPoints}
                        onChange={(e) => setUpdatedPoints(e.target.value)}
                    />
                ) : (
                    participant.points
                )}
            </td>

            <td>
                {isModified ? (
                    <>
                        <button onClick={handleSubmit}>Save</button>
                        <button onClick={handleCancel}>Cancel</button>
                    </>
                ) : (
                    <>
                        <button onClick={handleDelete}>Delete</button>
                        <button onClick={handleEdit}>Update</button>
                    </>
                )}
            </td>

        </tr>
    )
}

export default function ParticipantTable({participants, deleteFunc, updateFunc}){
    console.log("In ParticipantTable");
    console.log(participants);
    let rows = [];
    participants.forEach(function (participant){
        console.log("PARTICIPANT   " + participant);
        rows.push(<ResultRow participant={participant} key={participant.id} deleteFunc={deleteFunc} updateFunc={updateFunc} />)
    });

    return (
        <div className="ParticipantTable">

            <table className="center">
                <thead>
                <tr>
                    <th>Id</th>
                    <th>FirstName</th>
                    <th>LastName</th>
                    <th>Points</th>

                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>{rows}</tbody>
            </table>

        </div>
    );
}