import api from './auth'

export const commentApi = {
  addComment(data) {
    return api.post('/comment/add', data)
  },
  getComments(contentId, page = 1, size = 5) {
    return api.get('/comment/list', { params: { contentId, page, size } })
  },
  getReplies(parentId) {
    return api.get('/comment/replies', { params: { parentId } })
  },
  deleteComment(commentId) {
    return api.delete(`/comment/${commentId}`)
  }
}

export default commentApi 