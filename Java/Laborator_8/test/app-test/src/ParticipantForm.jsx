import React from "react";
import {useState} from "react";
export default function ParticipantForm({addFunc}){
    const [idpart, setIdpart] = useState('');
    const [fnamepart, setFnamepart] = useState('');
    const [lnamepart, setLnamepart] = useState('');
    const [points, setPoints] = useState('');


    function handleSubmit(event){
        let participant = {
            first_name: fnamepart,
            last_name: lnamepart,
            points: points
        }
        console.log('A participant was submitted: ');
        console.log(participant);
        addFunc(participant);
        event.preventDefault();
    }return(
        <form onSubmit={handleSubmit}>

            <label>
                First Name:
                <input type = "text" value={fnamepart} onChange={e=>setFnamepart(e.target.value)}/>
            </label><br/>

            <label>
                Last Name:
                <input type = "text" value={lnamepart} onChange={e=>setLnamepart(e.target.value)}/>
            </label><br/>

            <label>
                Points:
                <input type = "text" value={points} onChange={e=>setPoints(e.target.value)}/>
            </label><br/>


            <input type="submit" value="Add Participant" />

        </form>
    )
}