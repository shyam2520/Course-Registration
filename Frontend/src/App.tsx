
import './App.css'
import { Button } from './components/ui/button'
import { columns } from './components/ui/Table/Columns'
import { DataTable } from './components/ui/Table/dataTable'

function App() {

  return (
    <>
      <Button>Course Registartion</Button>
      <DataTable columns={columns} data={[]}></DataTable>
    </>
  )
}

export default App
