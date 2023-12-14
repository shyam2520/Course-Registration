import React from 'react';
import FullCalendar from '@fullcalendar/react'
import dayGridPlugin from '@fullcalendar/daygrid'

const TimeTable: React.FC = () => {
    return (
        <div style={{'maxHeight':'100px'}}>
            {/* Your TimeTable component code goes here */}
            <FullCalendar
                plugins={[dayGridPlugin]}
                // style={calendarStyle}
                initialView="dayGridMonth"
                events={[
                        { 
                        title: 'Meeting 1',
                        start: '2023-01-01T10:00:00', // ISO 8601 format: YYYY-MM-DDTHH:mm:ss
                        end: '2023-01-01T11:30:00',
                      },
                      {
                        title: 'Lunch',
                        start: '2023-01-01T12:00:00',
                        end: '2023-01-01T13:00:00',
                      },
                      {
                        title: 'Meeting 2',
                        start: '2023-01-02T14:00:00',
                        end: '2023-01-02T15:30:00',
                      }
                ]} />
        </div>
    );
};

export default TimeTable;
