import React, {useEffect} from 'react';
import {useState} from 'react';
import ParticipantTable from './ParticipantTable.jsx'
import './App.css'
import ParticipantForm from './ParticipantForm.jsx';
import {GetParticipants, DeleteParticipants, AddParticipant, UpdateParticipant} from './utils/rest-calls.js'

export default function App(){
    const [participants, setParticipants] = useState([
         {
            "first_name": "Andrada",
            "last_name": "Muresan",
            "points": 130,
            "id": 1
        }]);
    console.log("IN APP.JSX");

    function addFunc(participant){
        console.log("IN APP.JSX - ADD");
        console.log('inside Add Func ' + participant);
        AddParticipant(participant)
            .then(part=>GetParticipants())
            .then(participants=>setParticipants(participants))
            .catch(error=>console.log('eroare add ', error));
    }

    function deleteFunc(participant){
        console.log('inside deleteFunc '+participant);
        DeleteParticipants(participant)
            .then(part=> GetParticipants())
            .then(participants=>setParticipants(participants))
            .catch(error=>console.log('eroare delete', error));
    }

    function updateFunc(participant){
        console.log('inside update Func '+participant);
        UpdateParticipant(participant)
            .then(()=> GetParticipants())
            .then(artists=>setParticipants(artists))
            .catch(erorr=>console.log('eroare update ',erorr));
    }

    useEffect(()=>{
        console.log('inside useEffect')
        GetParticipants().then(participants=>setParticipants(participants));},[]);

    return (<div className="App">
        <h1> New Participant Management App </h1>
        <ParticipantForm addFunc={addFunc}/>
        <br/>
        <br/>
        <ParticipantTable participants={participants} deleteFunc={deleteFunc} updateFunc={updateFunc}/>
    </div>);
}