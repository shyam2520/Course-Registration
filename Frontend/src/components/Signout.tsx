
import { getSignOut } from '@/store/AuthStore';
import { Button } from './ui/button'

export default function Signout() {

  const signOut = getSignOut();

  return (
    <Button variant={'secondary'} onClick={signOut}>
      Sign Out
    </Button>
  )
}
