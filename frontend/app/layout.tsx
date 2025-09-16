import './globals.css'
import type { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'E-Commerce Store',
  description: 'Modern e-commerce application',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en">
      <body>{children}</body>
    </html>
  )
}