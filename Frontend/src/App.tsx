
import { QueryClient, QueryClientProvider } from 'react-query';
import './App.css';
import AuthProvider from './providers/AuthProvider';
import { Route } from './providers/Route';
import { Button } from './components/ui/button'
import { columns } from './components/ui/Table/Columns'
import { DataTable } from './components/ui/Table/dataTable'

function App() {

  const queryClient = new QueryClient();

  return (
    <>
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <Route />
      </AuthProvider>
    </QueryClientProvider>
      <Button>Course Registartion</Button>
      <DataTable columns={columns} data={[]}></DataTable>
    </>
  )
}

export default App
